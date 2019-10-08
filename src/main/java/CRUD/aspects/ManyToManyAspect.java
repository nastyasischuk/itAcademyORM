package CRUD.aspects;

import annotations.AnnotationUtils;
import annotations.Entity;
import annotations.ManyToMany;
import annotations.PrimaryKey;
import connection.DataBase;
import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashSet;


@Aspect
public class ManyToManyAspect {
    protected static org.apache.log4j.Logger logger = Logger.getLogger(ManyToManyAspect.class);
    private static DataBase dataBase;

    @Pointcut("execution(* get*(..))")
    public void ManyToManyPointCutDefinition() {
    }

    @Before("ManyToManyPointCutDefinition()")
    public void findInDataBaseIfFieldsNull(JoinPoint pointcut) {
        Object caller = pointcut.getTarget();
        String signature = pointcut.getSignature().getName();
        String collectionName = signature.substring(3).toLowerCase();
        try {
            if (caller == null || !caller.getClass().isAnnotationPresent(Entity.class)
                    || !caller.getClass().getDeclaredField(collectionName).isAnnotationPresent(ManyToMany.class))
                return;
        } catch (NoSuchFieldException e) {
            logger.error(e, e.getCause());
        }
        logger.debug("Checking many to many collection " + collectionName);
        Field[] fields = caller.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            if (field.getName().equals(collectionName)) {
                try {
                    Collection collection = (Collection<Object>) field.get(caller);
                    handleFindingObject(caller, collection);
                } catch (IllegalAccessException e) {
                    logger.error(e, e.getCause());
                }
            }
        }

    }

    private void handleFindingObject(Object owner, Collection<Object> collection) {
        Collection<Object> collection1 = new HashSet<>();
        Collection<Object> collectionTORemove = new HashSet<>();
        for (Object o1 : collection) {
            if (checkObjectInCollectionIsNull(o1)) {
                collectionTORemove.add(o1);
                o1 = findObjectInDB(o1);
                logger.debug("Loading object in many to many collection from db  " + o1);
                collection1.add(o1);
                linkCollections(owner, o1);
            }
        }
        collection.removeAll(collectionTORemove);
        collection.addAll(collection1);
    }

    private void linkCollections(Object owner, Object o) {
        Field fields[] = o.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(ManyToMany.class)
                    && (AnnotationUtils.classGetTypeOfCollectionField(field) == owner.getClass())) {
                field.setAccessible(true);
                try {
                    Collection<Object> collection = (Collection<Object>) field.get(o);
                    addInitialObjectToCollection(owner, collection);
                } catch (IllegalAccessException e) {
                    logger.error(e, e.getCause());
                }
            }

        }
    }

    private void addInitialObjectToCollection(Object owner, Collection<Object> collection) {
        Object toReplace = null;
        for (Object inCollection : collection) {
            findPK(inCollection).equals(findPK(owner));
            toReplace = inCollection;
            break;
        }
        collection.remove(toReplace);
        collection.add(owner);
    }

    private Object findObjectInDB(Object o) {
        Object pk = findPK(o);
        Class classOfObject = o.getClass();
        return dataBase.getCrud().find(classOfObject, pk);
    }


    private Object findPK(Object objectToFind) {
        Object PK;
        Field[] fields = objectToFind.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(PrimaryKey.class)) {
                field.setAccessible(true);
                try {
                    PK = field.get(objectToFind);
                    return PK;
                } catch (IllegalAccessException e) {
                    logger.error(e, e.getCause());
                }
            }
        }
        return null;
    }

    private boolean checkObjectInCollectionIsNull(Object object) {
        Field fields[] = object.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            if (field.isAnnotationPresent(ManyToMany.class)) {
                try {
                    if (field.get(object) == null) {
                        return true;
                    }
                } catch (IllegalAccessException e) {
                    logger.error(e, e.getCause());
                }
            }
        }
        return false;
    }

    public static void setDb(DataBase dB) {
        dataBase = dB;
    }
}

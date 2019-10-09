package CRUD.aspects;

import annotations.AnnotationUtils;
import annotations.Entity;
import annotations.ManyToMany;
import connection.DataBase;
import exceptions.NoPrimaryKeyException;
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
    private static org.apache.log4j.Logger logger = Logger.getLogger(ManyToManyAspect.class);
    private static DataBase dataBase;

    @Pointcut("execution(* get*(..))")
    public void manyToManyPointCutDefinition() {
    }

    @Before("manyToManyPointCutDefinition()")
    public void findInDataBaseIfFieldsNull(JoinPoint pointcut) {
        Object caller = pointcut.getTarget();
        String signature = pointcut.getSignature().getName();
        String collectionName = signature.substring(3).toLowerCase();
        try {
            if (!isManyToManyGetter(caller, collectionName)) return;
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

    private boolean isManyToManyGetter(Object caller, String collectionName) throws NoSuchFieldException {
        return caller != null && caller.getClass().isAnnotationPresent(Entity.class)
                && caller.getClass().getDeclaredField(collectionName).isAnnotationPresent(ManyToMany.class);
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
        Field[] fields = o.getClass().getDeclaredFields();
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
            try {
                if (findPK(inCollection).equals(findPK(owner))) {
                    toReplace = inCollection;
                    break;
                }
            } catch (NoPrimaryKeyException e) {
                logger.error(e.getMessage(), e);
            }
        }
        collection.remove(toReplace);
        collection.add(owner);
    }

    private Object findObjectInDB(Object o) {
        Object pk = null;
        try {
            pk = findPK(o);
        } catch (NoPrimaryKeyException e) {
            logger.error(e.getMessage());
        }
        Class classOfObject = o.getClass();
        return dataBase.getCrud().find(classOfObject, pk);
    }

    private Object findPK(Object objectToFind) throws NoPrimaryKeyException {
        Object primaryKey;
        Field[] fields = objectToFind.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (AnnotationUtils.isPrimaryKeyPresent(field)) {
                field.setAccessible(true);
                try {
                    primaryKey = field.get(objectToFind);
                    return primaryKey;
                } catch (IllegalAccessException e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
        throw new NoPrimaryKeyException();
    }

    private boolean checkObjectInCollectionIsNull(Object object) {
        Field[] fields = object.getClass().getDeclaredFields();
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

package CRUDdb.rowhandler;

import annotations.*;

import java.lang.reflect.Field;

public class RowConstructorFromDBByForeignKey extends RowConstructorFromDB {

    public RowConstructorFromDBByForeignKey(Class typeOfObject, Object id) {
        super(typeOfObject, id);
    }

    @Override
    protected String getIdName() {
        Field[] fields = super.getTypeOfObject().getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(ForeignKey.class) || field.isAnnotationPresent(OneToMany.class) || field.isAnnotationPresent(ManyToOne.class))
                return getNameOfField(field);
        }
        throw new RuntimeException("No pk?");//todo logger exception
    }
}

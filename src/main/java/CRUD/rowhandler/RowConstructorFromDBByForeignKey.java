package CRUD.rowhandler;

import annotations.*;

import java.lang.reflect.Field;

public class RowConstructorFromDBByForeignKey extends RowConstructorFromDB{
    private Class typeUsesClassAsForeignkey;
    public RowConstructorFromDBByForeignKey(Class typeOfObject, Object id,Class typeUsesClassAsForeignkey) {
        super(typeOfObject, id);
        this.typeUsesClassAsForeignkey = typeUsesClassAsForeignkey;
    }

    @Override
    protected String getIdName() {
        Field[] fields = super.getTypeOfObject().getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(ForeignKey.class )|| field.isAnnotationPresent(OneToMany.class ) || field.isAnnotationPresent(ManyToOne.class))
                return getNameOfField(field);
            }
        throw new RuntimeException("No pk?");//todo logger exception
    }
}

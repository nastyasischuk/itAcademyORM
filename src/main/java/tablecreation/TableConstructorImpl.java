package tablecreation;

import annotations.Check;
import annotations.Index;
import exceptions.NoPrimaryKeyException;
import exceptions.WrongSQLType;

import java.lang.reflect.Field;
import java.util.ArrayList;

import java.util.List;

public class TableConstructorImpl implements TableConstructor {
    Class<?> toBuildClass;
    Table table;


    public TableConstructorImpl(Class<?> toBuildClass) {
        this.table = new Table(getTableName());
        this.toBuildClass = toBuildClass;
    }

    @Override
    public Table buildTable() {
       table.setColumns(getColumns());
       addcheckConstraintIfExists();
        return table;
    }

    private String getTableName(){
        if(toBuildClass.isAnnotationPresent(annotations.Table.class)){
            return toBuildClass.getAnnotation(annotations.Table.class).name();
        }else{
            return toBuildClass.getSimpleName();
        }

    }
    private void addcheckConstraintIfExists(){
        if(toBuildClass.isAnnotationPresent(Check.class)){
            table.setCheckConstraint(toBuildClass.getAnnotation(Check.class).value());       }
    }

    private List<Column> getColumns(){
        Field[] classFields = toBuildClass.getDeclaredFields();
        List<Column> columns  = new ArrayList<>();
        for(int i=0;i<classFields.length;i++){
            //todo check if column
            if(!classFields[i].isAnnotationPresent(annotations.Column.class) ||
                    !classFields[i].isAnnotationPresent(annotations.ForeignKey.class) )
               continue;
            Column builtColumn = null;
            try {
                builtColumn = new ColumnConstructor(classFields[i]).buildColumn();
            }catch (WrongSQLType e){
                continue;
            }
            columns.add(builtColumn);
            if(builtColumn.isForeignKey()){
            formFK(classFields[i]);
            }
            if(builtColumn.isPrimaryKey()){
                table.setPrimaryKey(formPK(builtColumn));
            }
            if(classFields[i].isAnnotationPresent(Index.class)){
                tablecreation.Index indexToTable = generateIndex(classFields[i]);
                indexToTable.addColumns(builtColumn);
            }


        }
        checkIfPrimaryKeyPresent();//todo check
        return columns;
    }
   private PrimaryKey formPK(Column column){
       PrimaryKey primaryKey;
        if(table.getPrimaryKey()!=null){
          primaryKey = table.getPrimaryKey();
       }else{
            primaryKey = new PrimaryKey();
        }
        primaryKey.addPrimaryKey(column);
        return primaryKey;
   }
   private ForeignKey formFK(Field field){
        return new ForeignKeyConstructorImpl(field).buildForeignKey();
   }
   private tablecreation.Index generateIndex(Field field){
       tablecreation.Index indexTOCreate = new tablecreation.Index(field.getAnnotation(Index.class).name(),field.getAnnotation(Index.class).unique());
       if(table.getIndexes().contains(indexTOCreate)){
           indexTOCreate = table.getIndexes().get(table.getIndexes().indexOf(indexTOCreate));
       }
       return indexTOCreate;
   }
   private void checkIfPrimaryKeyPresent(){
        if(table.getPrimaryKey()==null)
            throw new NoPrimaryKeyException();
   }


}

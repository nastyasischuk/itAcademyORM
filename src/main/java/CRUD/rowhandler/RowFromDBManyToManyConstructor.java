package CRUD.rowhandler;


import annotations.AssociatedClass;

public class RowFromDBManyToManyConstructor extends RowConstructorFromDB {
   private RowFromDBManyToMany row;
    public RowFromDBManyToManyConstructor(Class typeOfObject, Object id, AssociatedClass associatedClass) {
        super(typeOfObject, id);
        row  = new RowFromDBManyToMany();
        readInfFromAssociateTable(associatedClass);
    }

    @Override
    public RowFromDBManyToMany buildRow() {
        operationsToBuild(row);
        return row;
    }
    private void readInfFromAssociateTable(AssociatedClass associatedClass){
        row.setManyToManyTableName(associatedClass.getAssociatedTableName());
        row.setJoinCoulmn(associatedClass.getJoinColumn());
        row.setInverseColumn(associatedClass.getInverseJoinColumns());
    }
}

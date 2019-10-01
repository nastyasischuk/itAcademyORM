package CRUD.rowhandler;

import annotations.AssociatedTable;

public class RowFromDBManyToManyConstructor extends RowConstructorFromDB {
   private RowFromDBManyToMany row;
    public RowFromDBManyToManyConstructor(Class typeOfObject, Object id, AssociatedTable associatedTable) {
        super(typeOfObject, id);
        row  = new RowFromDBManyToMany();
        readInfFromAssociateTable(associatedTable);
    }

    @Override
    public RowFromDBManyToMany buildRow() {
        operationsToBuild(row);
        return row;
    }
    private void readInfFromAssociateTable(AssociatedTable associatedTable){
        row.setManyToManyTableName(associatedTable.associatedTableName());
        row.setJoin_coulmn(associatedTable.joinColumns().name());
        row.setInverse_column(associatedTable.inverseJoinColumns().name());
    }
}

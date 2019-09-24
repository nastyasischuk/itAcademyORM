package tablecreation;

public class SQLTableQueryCreator {
    private ForeignKey fk;
    private Table table;


    String createTableQuery(){
        StringBuilder request = new StringBuilder();
        request.append(SQLStatements.CREATETABLE).append(fk.getTableName()).append("\n");
        for(Column column: table.getColumns()){
            request.append(column.getName()).append(" ").append(column.getType().toString()).append("\n");
            if(column.isNullable()){
                request.append(SQLStatements.NOT_NULL);
            }if(column.isUnique()){
                request.append(SQLStatements.UNIQUE);
            }if(column.isAutoincrement()){
                request.append(SQLStatements.A_INCREMENT);
            }
        }
        return request.toString();
    }

    public String createFKQuery(){
        StringBuilder request = new StringBuilder();
        request.append(SQLStatements.ALTER).append(fk.getTableName()).append("\n")
                .append(SQLStatements.ADD).append(SQLStatements.CONSTRAINT).append(fk.getConstructionName()).append("\n")
                .append(SQLStatements.FK).append('(').append(fk.getForeignKeyName()).append(')')
                .append(SQLStatements.REFERENCE).append(fk.getReferenceTableName())
                .append('(').append(fk.getReferencePKName()).append(')');
        return request.toString();
    }

    public String createPKQuery(){
        PrimaryKey pk = new PrimaryKey();
        StringBuilder request = new StringBuilder();
        StringBuilder columnNames = new StringBuilder();
        request.append(SQLStatements.ALTER).append(fk.getTableName()).append("\n")
                .append(SQLStatements.ADD);
        if(pk.getPKList().size() > 1){
            request.append(SQLStatements.CONSTRAINT).append("default");
        }
        for (Column column: pk.getPKList()){
            columnNames.append(column.getName());
            if(pk.getPKList().isEmpty()){
                break;
            }else{
                columnNames.append(", ");
            }
        }
                request.append(SQLStatements.PK).append('(').append(columnNames).append(')');
        return request.toString();
    }

}

package tablecreation;

public class SQLQueryFormer {
    private static final String createTable= "create table";
    private static final String dropIfExists= "drop table if exist";
    private static final String notnull = "not null";

    private Table tableToCreate;

    public SQLQueryFormer(Table table){
        this.tableToCreate = table;
    }

    private String columnQueryFormer(Column column){
        String finalQuery  = "";
        return finalQuery;
    }

}

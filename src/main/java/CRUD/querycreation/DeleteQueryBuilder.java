package CRUD.querycreation;


import CRUD.rowhandler.RowToDB;

public class DeleteQueryBuilder extends QueryBuilder{
    public DeleteQueryBuilder(RowToDB row) {
        super(row);
    }

    @Override
    public String buildQuery() {
        return null;
    }
}

package CRUD.querycreation;

import CRUDdb.querycreation.QueryBuilderFromDBManyToMany;
import CRUDdb.rowhandler.RowFromDBManyToMany;
import CRUDdb.rowhandler.RowFromDBManyToManyConstructor;
import annotations.AnnotationUtils;
import annotations.AssociatedTable;
import org.junit.Test;
import tablecreation.classesintesting.CatTestManyToMany;
import tablecreation.classesintesting.PersonTestManyToMany;

import static org.junit.Assert.*;

public class QueryBuilderFromDBManyToManyTest {

    @Test
    public void buildQuery() throws Exception{
        PersonTestManyToMany person = new PersonTestManyToMany(1,"Kate",20);
        RowFromDBManyToMany row = new RowFromDBManyToManyConstructor(CatTestManyToMany.class,1, AnnotationUtils.getAssociatedTable(person.getClass().getDeclaredField("cats"))).buildRow();
        String query = new QueryBuilderFromDBManyToMany(row).buildQuery();
        assertEquals(" SELECT c_id FROM cat_person WHERE p_id=1;",query);
    }
}
package CRUD.querycreation;

import CRUD.rowhandler.RowFromDBManyToMany;
import CRUD.rowhandler.RowFromDBManyToManyConstructor;
import annotations.AnnotationUtils;
import org.junit.Test;
import tablecreation.classesintesting.CatTestManyToMany;
import tablecreation.classesintesting.PersonTestManyToMany;

import static org.junit.Assert.*;

public class QueryBuilderFromDBManyToManyTest {

    @Test
    public void buildQuery() throws Exception{
        PersonTestManyToMany person = new PersonTestManyToMany(1,"Kate");
        RowFromDBManyToMany row = new RowFromDBManyToManyConstructor(CatTestManyToMany.class,1, AnnotationUtils.getAssociatedTable(person.getClass().getDeclaredField("cats"))).buildRow();
        String query = new QueryBuilderFromDBManyToMany(row).buildQuery();
        assertEquals(" SELECT c_id FROM cat_person WHERE p_id=1;",query);
    }
}
package CRUD.rowhandler;

import CRUDdb.rowhandler.RowFromDBManyToMany;
import CRUDdb.rowhandler.RowFromDBManyToManyConstructor;
import annotations.AnnotationUtils;
import annotations.AssociatedTable;
import org.junit.Test;
import tablecreation.classesintesting.CatTestManyToMany;
import tablecreation.classesintesting.PersonTestManyToMany;

import static org.junit.Assert.*;

public class RowFromDBManyToManyConstructorTest {

    @Test
    public void buildRowTestPoperNameOfColumnsInManyToManyTable() throws Exception{
        PersonTestManyToMany person = new PersonTestManyToMany(1,"Kate");
        RowFromDBManyToMany row = new RowFromDBManyToManyConstructor(CatTestManyToMany.class,1, AnnotationUtils.getAssociatedTable(person.getClass().getDeclaredField("cats"))).buildRow();
        assertNotEquals(row.getTableName(),"PersonTestManyToMany");
        assertEquals("c_id",row.getInverse_column());
        assertEquals("p_id",row.getJoin_coulmn());
    }
    @Test
    public void buildRowTestProperNameAndVlueOfId() throws Exception{
        PersonTestManyToMany person = new PersonTestManyToMany(1,"Kate");
        RowFromDBManyToMany row = new RowFromDBManyToManyConstructor(CatTestManyToMany.class,1,AnnotationUtils.getAssociatedTable(person.getClass().getDeclaredField("cats"))).buildRow();

        assertEquals("1",row.getIdValue());
        assertEquals("id",row.getIdName());//id name from second table
        assertEquals("p_id",row.getJoin_coulmn());
        assertEquals("c_id",row.getInverse_column());
    }
    @Test
    public void buildRowTestProperColumnsInTable() throws Exception{
        PersonTestManyToMany person = new PersonTestManyToMany(1,"Kate");
        RowFromDBManyToMany row = new RowFromDBManyToManyConstructor(CatTestManyToMany.class,1,AnnotationUtils.getAssociatedTable(person.getClass().getDeclaredField("cats"))).buildRow();

        assertTrue(row.getNameAndType().containsKey("name"));
        assertTrue(row.getNameAndType().containsKey("person"));
        assertTrue(row.getNameAndType().containsKey("id"));
    }
}
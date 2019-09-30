package CRUD.rowhandler;

import annotations.AssociatedTable;
import org.junit.Test;
import tablecreation.classesintesting.CatTestManyToMany;
import tablecreation.classesintesting.PersonTestManyToMany;

import static org.junit.Assert.*;

public class RowFromDBManyToManyConstructorTest {

    @Test
    public void buildRowTestPoperNameOfColumnsInManyToManyTable() throws Exception{
        PersonTestManyToMany person = new PersonTestManyToMany(1,"Kate",20);
        RowFromDBManyToMany row = new RowFromDBManyToManyConstructor(CatTestManyToMany.class,1,person.getClass().getDeclaredField("cats").getAnnotation(AssociatedTable.class)).buildRow();
        assertNotEquals(row.getTableName(),"PersonTestManyToMany");
        assertEquals("c_id",row.getInverse_column());
        assertEquals("p_id",row.getJoin_coulmn());
    }
    @Test
    public void buildRowTestProperNameAndVlueOfId() throws Exception{
        PersonTestManyToMany person = new PersonTestManyToMany(1,"Kate",20);
        RowFromDBManyToMany row = new RowFromDBManyToManyConstructor(CatTestManyToMany.class,1,person.getClass().getDeclaredField("cats").getAnnotation(AssociatedTable.class)).buildRow();

        assertEquals("1",row.getIdValue());
        assertEquals("id",row.getIdName());
    }
    @Test
    public void buildRowTestProperColumnsInTable() throws Exception{
        PersonTestManyToMany person = new PersonTestManyToMany(1,"Kate",20);
        RowFromDBManyToMany row = new RowFromDBManyToManyConstructor(CatTestManyToMany.class,1,person.getClass().getDeclaredField("cats").getAnnotation(AssociatedTable.class)).buildRow();

        assertTrue(row.getNameAndType().containsKey("nameCat"));
        assertTrue(row.getNameAndType().containsKey("person"));
        assertTrue(row.getNameAndType().containsKey("id"));
    }
}
package CRUD.rowhandler;

import CRUDdb.rowhandler.RowConstructorFromDB;
import CRUDdb.rowhandler.RowFromDB;
import org.junit.Test;
import tablecreation.classesintesting.PersonOneFieldWithoutColumnAnnotation;

import static org.junit.Assert.*;

public class RowConstructorFromDBTest {

    @Test
    public void buildRowTestAppropriateFields() {
        RowFromDB row = new RowConstructorFromDB(PersonOneFieldWithoutColumnAnnotation.class,1).buildRow();
        assertFalse(row.getNameAndType().containsKey("age"));
        assertTrue(row.getNameAndType().containsKey("id"));
    }
    @Test
    public void buildRowTestAppropriateFieldsPrimaryKey() {
        RowFromDB row = new RowConstructorFromDB(PersonOneFieldWithoutColumnAnnotation.class,1).buildRow();
        assertFalse(row.getNameAndType().containsKey("age"));
        assertTrue(row.getNameAndType().containsKey("id"));
    }
}
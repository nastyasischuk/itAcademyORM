package CRUD.rowhandler;


import org.junit.Test;
import tablecreation.classesintesting.PersonWithConstraints;

import static org.junit.Assert.*;

public class RowToDBTest {
    @Test
    public void isAutoIncrement() {
        PersonWithConstraints person = new PersonWithConstraints(1,"Kate",12);
        RowToDB rowToDb = new RowConstructorToDB(person).buildRow();
        assertTrue(rowToDb.isAutoIncrement());
    }
}
package CRUD.rowhandler;

import CRUDdb.rowhandler.RowConstructorToDB;
import org.junit.Test;
import tablecreation.classesintesting.PersonWithIndexes;
import tablecreation.classesintesting.PersonWithSeveralIndexes;

import static org.junit.Assert.*;

public class RowConstructorTest {
    @Test
    public void getTableNameTestNameInTable(){
        PersonWithIndexes person = new PersonWithIndexes(1,"sb",20);
        String tableName = new RowConstructorToDB(person).getTableName(person.getClass());
        assertEquals("person",tableName);
    }
    @Test
    public void getTableNameTestDefaultNameOfTable(){
        PersonWithSeveralIndexes person = new PersonWithSeveralIndexes(1,"sb",20);
        String tableName = new RowConstructorToDB(person).getTableName(person.getClass());
        assertEquals("PersonWithSeveralIndexes",tableName);
    }

}
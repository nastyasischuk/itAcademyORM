package CRUD.rowhandler;

import CRUDdb.rowhandler.RowConstructorFromDBByForeignKey;
import CRUDdb.rowhandler.RowFromDB;
import org.junit.Test;
import tablecreation.classesintesting.PersonWithConstraints;
import tablecreation.classesintesting.PersonWithSeveralForeignKeys;

import static org.junit.Assert.*;

public class RowConstructorFromDBByForeignKeyTest {

    @Test
    public void getIdName() {
        PersonWithSeveralForeignKeys person = new PersonWithSeveralForeignKeys(1,"sb",20);
       person.setPerson1(new PersonWithConstraints(1,"kate",22));
        RowFromDB row= new RowConstructorFromDBByForeignKey(person.getClass(),1, PersonWithConstraints.class).buildRow();
        assertEquals("per_constr",row.getIdName());
        assertEquals("1",row.getIdValue());
    }
}
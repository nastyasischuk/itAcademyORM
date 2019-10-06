package CRUD.rowhandler;

import org.junit.Test;
import tablecreation.classesintesting.CatManyToOne;
import tablecreation.classesintesting.PersonOneToMany;
import tablecreation.classesintesting.PersonWithSimpleProperColumns;

import java.sql.Date;

import static org.junit.Assert.*;

public class RowConstructorToDBTest {

    @Test
    public void buildRowBasicTestProperId() {
        Date sqlSate  = new Date(System.currentTimeMillis());
        PersonWithSimpleProperColumns person = new PersonWithSimpleProperColumns(1,"Kate",20,sqlSate);
        RowConstructorToDB rowConstructor = new RowConstructorToDB(person);
        Row row = rowConstructor.buildRow();
        assertEquals("1",row.getIdValue());
        assertEquals("p_id",row.getIdName());
    }
    @Test
    public void buildRowTestDataTypes() {
        Date sqlSate  = new Date(System.currentTimeMillis());
        PersonWithSimpleProperColumns person = new PersonWithSimpleProperColumns(1,"Kate",20,sqlSate);
        RowConstructorToDB rowConstructor = new RowConstructorToDB(person);
        RowToDB row = rowConstructor.buildRow();
        assertTrue(row.getMap().containsKey("bd"));
        assertEquals(sqlSate.toString(),row.getMap().get("bd"));
    }
    @Test
    public void buildRowTestNullValues() {
        PersonWithSimpleProperColumns person = new PersonWithSimpleProperColumns(1,"Kate",20,null);
        RowConstructorToDB rowConstructor = new RowConstructorToDB(person);
        RowToDB row = rowConstructor.buildRow();
        assertTrue(row.getMap().containsKey("bd"));
    }
    @Test
    public void buildRowTestNullValuesManyTOOne() {
        CatManyToOne oneToMany = new CatManyToOne(14, "new Name", new PersonOneToMany(14, ""));
        RowConstructorToDB rowConstructor = new RowConstructorToDB(oneToMany);
        RowToDB row = rowConstructor.buildRow();
        assertEquals(row.getMap().get("person"),"14");
    }
}
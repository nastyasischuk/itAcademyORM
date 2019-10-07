package CRUD;

import fluentquery.Dslmpl.QueryOrderedImpl;
import fluentquery.Dslmpl.TableForQuery;
import connection.DataBase;
import connection.DataBaseImplementation;
import customQuery.QueryResult;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import tablecreation.classesintesting.Person;

import java.util.List;

public class DSLQueryTest {
    private static  DataBase dataBase;

    @BeforeClass
    public static void setDataBase() {
        dataBase = new DataBaseImplementation("C:\\Users\\DEDUSHKA DEDULYA\\IdeaProjects\\itAcademyORM\\src\\main\\resources\\config.xml", false);
    }

    @Test
    public void queryCreationTest() {
        TableForQuery person = new TableForQuery(Person.class);
        dataBase.openConnection();
        QueryOrderedImpl start = new QueryOrderedImpl(dataBase, Person.class);
        start.select().from(person.table()).where(person.field("salary").greater(12)).end();
        QueryResult<Person> custQuery = new QueryResult<>(start);
        List<Person> personList = custQuery.getListOfFoundObjects();

        dataBase.close();
        Assert.assertEquals(2, personList.size());
    }
}

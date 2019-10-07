package customQuery;

import connection.DataBase;
import connection.DataBaseImplementation;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import tablecreation.classesintesting.PersonOneToMany;

import java.util.List;

import static org.junit.Assert.*;

public class QueryTest {
    private static Logger logger = Logger.getLogger(QueryResultTest.class);
    DataBase dataBase ;
    @Before
    public void setDataBase() {
        dataBase = new DataBaseImplementation("D:\\my files\\softserve\\itAcademyORM\\src\\main\\resources\\config.xml", "catspeople", false);
        dataBase.openConnection();
    }
    @Test
    public void getSingleObject() {
        QueryBuilder query = dataBase.getQueryBuilder(PersonOneToMany.class);
        query.select().where(query.getLimits().equals("id", "1")).fetch();
        Query<PersonOneToMany> custQuery = new Query<>(query);
        PersonOneToMany person = custQuery.getSingleObject();
        assertEquals(1,person.getId());
        assertEquals(2,person.getCollectrion().size());
    }

    @Test
    public void getListOfFoundObjects() {
        QueryImpl query = new QueryImpl(PersonOneToMany.class,dataBase);
         query.select().fetch();
        Query<PersonOneToMany> custQuery = new Query<>(query);
        List<PersonOneToMany> person = custQuery.getListOfFoundObjects();
        System.out.println(person);
    }
}
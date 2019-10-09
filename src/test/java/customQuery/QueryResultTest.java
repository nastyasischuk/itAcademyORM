package customQuery;

import connection.DataBase;
import connection.DataBaseImplementation;
import org.apache.log4j.Logger;
import org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;
import tablecreation.classesintesting.PersonOneToMany;

import java.util.List;

import static org.junit.Assert.*;

public class QueryResultTest {
    private static Logger logger = Logger.getLogger(QueryResultTest.class);
   static DataBase dataBase ;
    @BeforeClass
    public static void setDataBase() {
        dataBase = new DataBaseImplementation("D:\\my files\\softserve\\itAcademyORM\\src\\main\\resources\\config.xml", "catspeople", false);
        dataBase.openConnection();
    }
    @Test
    public void getSingleObject() {
        QueryBuilderImpl query = dataBase.getQueryBuilder(PersonOneToMany.class);
       query.select().where(query.getLimits().equals("id", "1")).fetch();
        QueryResult<PersonOneToMany> custQueryResult = new QueryResult<>(query);
        PersonOneToMany person = custQueryResult.getSingleObject();
        assertEquals(1,person.getId());
    }

    @Test
    public void getListOfFoundObjects() {
        QueryBuilderImpl query =dataBase.getQueryBuilder(PersonOneToMany.class);
         query.select().fetch();
        QueryResult<PersonOneToMany> custQueryResult = new QueryResult<>(query);
        List<PersonOneToMany> person = custQueryResult.getListOfFoundObjects();
        assertEquals(4,person.size());
    }
}
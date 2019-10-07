package customQuery;

import annotations.AnnotationUtils;
import connection.DataBase;
import connection.DataBaseImplementation;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import tablecreation.classesintesting.PersonOneToMany;

import java.lang.reflect.Field;
import java.util.List;

public class QueryTest {
    private static Logger logger = Logger.getLogger(QueryTest.class);
   static  DataBase dataBase ;
    @BeforeClass
    public static void setDataBase() {
        dataBase = new DataBaseImplementation("D:\\my files\\softserve\\itAcademyORM\\src\\main\\resources\\config.xml", "catspeople", false);
    dataBase.openConnection();
    }
    @Test
    public void getSingleObject() {
        QueryBuilderImpl query = new QueryBuilderImpl(PersonOneToMany.class,dataBase);
        query.select().where(query.getLimits().equals("id", "1")).fetch();
        QueryResult<PersonOneToMany> custQuery = new QueryResult<>(query);
        PersonOneToMany person = custQuery.getSingleObject();
        Assert.assertEquals(1,person.getId());
    }

    @Test
    public void getListOfFoundObjects() {
        QueryBuilderImpl query = new QueryBuilderImpl(PersonOneToMany.class,dataBase);
        query.select().fetch();

        QueryResult<PersonOneToMany> custQuery = new QueryResult<>(query);
        List<PersonOneToMany> person = custQuery.getListOfFoundObjects();
        Assert.assertEquals(4,person.size());
    }
}
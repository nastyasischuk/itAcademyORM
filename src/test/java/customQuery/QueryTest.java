package customQuery;

import annotations.AnnotationUtils;
import connection.DataBase;
import connection.DataBaseImplementation;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import tablecreation.TableConstructorImpl;
import tablecreation.classesintesting.PersonOneToMany;
import tablecreation.classesintesting.PersonWithSimpleProperColumns;

import javax.xml.crypto.Data;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.Assert.*;

public class QueryTest {
    private static Logger logger = Logger.getLogger(QueryTest.class);
    static DataBase dataBase ;
    @BeforeClass
    public static void setDataBase() {
        dataBase = new DataBaseImplementation("D:\\my files\\softserve\\itAcademyORM\\src\\main\\resources\\config.xml", "catspeople", false);
    }
    @Test
    public void getSingleObject() {
        QueryImpl query = new QueryImpl(PersonOneToMany.class,dataBase);
        String actual = query.select().where(query.getLimits().equals("id", "1")).fetch();
        String expected = " SELECT * FROM PersonWithSimpleProperColumns WHERE p_id=1;";
        query.setQuery(new StringBuilder("SELECT * FROM person WHERE id=1"));
        Field[] fields = PersonOneToMany.class.getDeclaredFields();
        for (Field field : fields) {
            if (AnnotationUtils.isPrimaryKeyPresent(field)) {
                logger.info("exists");
            }
        }
        Query<PersonOneToMany> custQuery = new Query<>(query);
        PersonOneToMany person = custQuery.getSingleObject();
        System.out.println(person);
    }

    @Test
    public void getListOfFoundObjects() {
        QueryImpl query = new QueryImpl(PersonOneToMany.class,dataBase);
        String actual = query.select().where(query.getLimits().equals("id", "1")).fetch();
        query.setQuery(new StringBuilder("SELECT * FROM person"));

        Query<PersonOneToMany> custQuery = new Query<>(query);
        List<PersonOneToMany> person = custQuery.getListOfFoundObjects();
        System.out.println(person);
    }
}
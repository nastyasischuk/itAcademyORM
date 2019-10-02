package CRUD;

import CRUD.buildingObject.ObjectBuilder;
import CRUD.rowhandler.RowConstructorFromDB;
import CRUD.rowhandler.RowFromDB;
import org.junit.Test;
import sun.java2d.pipe.SpanShapeRenderer;
import tablecreation.classesintesting.PersonOneToMany;
import tablecreation.classesintesting.SimplePerson;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;

import static org.junit.Assert.*;

public class ObjectBuilderTest {





    @Test
    public void constructResultSetMethodName() {
        Class classToTest = Date.class;
        ObjectBuilder objectBuilder = new ObjectBuilder();
        String resultName = objectBuilder.constructResultSetMethodName(classToTest);
        assertNull(resultName);
    }

}
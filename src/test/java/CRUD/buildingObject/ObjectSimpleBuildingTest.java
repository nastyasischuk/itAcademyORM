package CRUD.buildingObject;

import org.apache.log4j.Logger;
import org.junit.Test;
import tablecreation.classesintesting.CatManyToOne;

import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetFactory;
import javax.sql.rowset.RowSetProvider;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import static org.junit.Assert.*;

public class ObjectSimpleBuildingTest {
        private static Logger logger = Logger.getLogger(ObjectSimpleBuildingTest.class);
        @Test
        public void buildObject() throws Exception {
            CatManyToOne object;
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/catspeople?autoReconnect=true&useSSL=false","root","0503236220");
            try (Statement statement = connection.createStatement()) {
                ResultSet resultSet = statement.executeQuery("Select * from catm");
                RowSetFactory factory = RowSetProvider.newFactory();
                CachedRowSet rowset = factory.createCachedRowSet();
                rowset.next();
                rowset.populate(resultSet);
                object = (CatManyToOne) new ObjectSimpleBuilding(rowset,CatManyToOne.class).buildObject();
                assertEquals(1,object.getId());
                assertNull(object.getPerson());
            }

        }
    }

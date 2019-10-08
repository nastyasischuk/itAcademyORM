package CRUD.buildingObject;

import org.apache.log4j.Logger;
import org.junit.Test;
import tablecreation.classesintesting.CatManyToOne;
import tablecreation.classesintesting.CatTestManyToMany;

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
   @Test
   public void buildObjectManyToMany() throws Exception {
       CatTestManyToMany object;
       Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/catspeople?autoReconnect=true&useSSL=false","root","0503236220");
       try (Statement statement = connection.createStatement()) {
           ResultSet resultSet = statement.executeQuery("SELECT c_id  FROM cat_person WHERE p_id=1");
           RowSetFactory factory = RowSetProvider.newFactory();
           CachedRowSet rowset = factory.createCachedRowSet();
           rowset.populate(resultSet);
           rowset.next();
           logger.info(rowset.getInt("c_id"));

          // object = (CatTestManyToMany) new ObjectSimpleBuilding(rowset, CatTestManyToMany.class).buildObject();
          // assertEquals(1,object.getId());
         //  assertNull(object.getPerson());
       }

   }
    }

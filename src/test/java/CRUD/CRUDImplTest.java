package CRUD;

import connection.DataBase;
import connection.DataBaseImplementation;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import tablecreation.TableConstructorImpl;
import tablecreation.classesintesting.CatManyToOne;
import tablecreation.classesintesting.CatTestManyToMany;
import tablecreation.classesintesting.PersonOneToMany;
import tablecreation.classesintesting.PersonTestManyToMany;

import static org.junit.Assert.*;

public class CRUDImplTest {
DataBaseImplementation dataBase;
    private static Logger logger = Logger.getLogger(CRUDImplTest.class);
    @Test
    public void findTestFindStartOneToMany() {
        CRUDImpl crud= dataBase.getCrud();
        PersonOneToMany object = (PersonOneToMany)crud.find(PersonOneToMany.class,1);
        for(CatManyToOne c :object.getCollectrion()){
           assertEquals(c.getPerson(),object);
        }
        logger.info(object.toString());


    }
    @Test
    public void findTestStartManyToOneWithNoDuplicatesInCollection() {
        CRUDImpl crud= dataBase.getCrud();
        CatManyToOne object = (CatManyToOne)crud.find(CatManyToOne.class,1);
        PersonOneToMany person = object.getPerson();
        logger.info(object.getPerson());
        logger.info(object.toString());
        for(CatManyToOne cat : person.getCollectrion()){
            if(cat.equals(object))
                logger.info("exists");
        }
        assertTrue(person.getCollectrion().contains(object));

    }

    @Test
    public void findCollection1() {
    }
    @Test
    public void findWithManyToMany(){
        PersonTestManyToMany person  = new PersonTestManyToMany(1,"Kate",20);
        person =(PersonTestManyToMany) dataBase.getCrud().find(person.getClass(),1);
        System.out.println(person);
    }
    @Before
    public void setDataBase(){
        dataBase = new DataBaseImplementation("D:\\my files\\softserve\\itAcademyORM\\src\\main\\resources\\config.xml","catspeople",false);
            dataBase.openConnection();

    }
    @After
    public void closeConncetion(){
        dataBase.close();
    }
}
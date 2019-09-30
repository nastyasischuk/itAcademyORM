package CRUD;

import connection.DataBase;
import connection.DataBaseImplementation;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import tablecreation.TableConstructorImpl;
import tablecreation.classesintesting.CatManyToOne;
import tablecreation.classesintesting.PersonOneToMany;

import static org.junit.Assert.*;

public class CRUDImplTest {
DataBaseImplementation dataBase;
    private static Logger logger = Logger.getLogger(CRUDImplTest.class);
    @Test
    public void find() {
        CRUDImpl crud= dataBase.getCrud();
        PersonOneToMany object = (PersonOneToMany)crud.find(PersonOneToMany.class,1);
        logger.info(object.toString());
        //assertEquals(null,object.toString());

    }
    @Test
    public void find2() {
        CRUDImpl crud= dataBase.getCrud();
        CatManyToOne object = (CatManyToOne)crud.find(CatManyToOne.class,2);
        logger.info(object.getPerson());
        logger.info(object.toString());
        //assertEquals(null,object.toString());

    }

    @Test
    public void findCollection() {
    }

    @Test
    public void findCollection1() {
    }
    @Before
    public void setDataBase(){
        dataBase = new DataBaseImplementation("D:\\my files\\softserve\\itAcademyORM\\src\\main\\resources\\config.xml","catspeople",false);
            dataBase.openConnection();
    }
}
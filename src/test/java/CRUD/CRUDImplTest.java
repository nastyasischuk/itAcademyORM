package CRUD;

import connection.DataBaseImplementation;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import tablecreation.classesintesting.CatManyToOne;
import tablecreation.classesintesting.PersonAI;
import tablecreation.classesintesting.PersonOneToMany;
import tablecreation.classesintesting.Person;

import static org.junit.Assert.*;

public class CRUDImplTest {
    DataBaseImplementation dataBase;

    private static Logger logger = Logger.getLogger(CRUDImplTest.class);
    @Test
    public void find() {
        CRUDImpl crud= dataBase.getCrud();
        PersonOneToMany object = (PersonOneToMany)crud.find(PersonOneToMany.class,1);
        for(CatManyToOne c :object.getCollectrion()){
           assertEquals(c.getPerson(),object);
        }
        logger.info(object.toString());


    }
    @Test
    public void find2() {
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
    public void savePersonWithoutAutoIncrement() {
        Person user = new Person();
        user.setId_person(1);
        user.setSalary(245);

        CRUDImpl crud = dataBase.getCrud();
        crud.save(user);
    }

    @Test
    public void savePersonWithAI() {
        PersonAI personAI = new PersonAI();
        personAI.setSalary(1234);

        CRUDImpl crud = dataBase.getCrud();
        crud.save(personAI);
        logger.debug("New id after auto increment is " + personAI.getId_person());
    }

    @Test
    public void findCollection1() {
    }
    @Before
    public void setDataBase(){
        dataBase = new DataBaseImplementation("C:\\Users\\DEDUSHKA DEDULYA\\IdeaProjects\\itAcademyORM\\src\\main\\resources\\config.xml","test",false);
            dataBase.openConnection();

    }
    @After
    public void closeConnection(){
        dataBase.close();
    }
}
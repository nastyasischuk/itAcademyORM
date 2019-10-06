package CRUD;

import CRUDdb.CRUD;
import CRUDdb.CRUDImpl;
import connection.DataBaseImplementation;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import tablecreation.classesintesting.*;
import transaction.TransactionsManager;

import static org.junit.Assert.*;

public class CRUDImplTest {
    DataBaseImplementation dataBase;

    private static Logger logger = Logger.getLogger(CRUDImplTest.class);

    @Test
    public void findTestFindStartOneToMany() {
        CRUDImpl crud = dataBase.getCrud();
        PersonOneToMany object = (PersonOneToMany) crud.find(PersonOneToMany.class, 1);
        for (CatManyToOne c : object.getCollectrion()) {
            assertEquals(c.getPerson(), object);
        }
        logger.info(object.toString());
    }

    @Test
    public void findTestStartManyToOneWithNoDuplicatesInCollection() {
        CRUDImpl crud = dataBase.getCrud();
        CatManyToOne object = (CatManyToOne) crud.find(CatManyToOne.class, 1);
        PersonOneToMany person = object.getPerson();
        assertTrue(person.getCollectrion().contains(object));
    }

    @Test
    public void findWithManyToMany() {
        PersonTestManyToMany personExpected = new PersonTestManyToMany(1, "Kate");
        PersonTestManyToMany personReal = (PersonTestManyToMany) dataBase.getCrud().find(personExpected.getClass(), 1);
        assertEquals(personExpected.getName(),personReal.getName());
    }

    @Before
    public void setDataBase() {
        dataBase = new DataBaseImplementation("D:\\my files\\softserve\\itAcademyORM\\src\\main\\resources\\config.xml", "catspeople", false);
        dataBase.openConnection();
    }

    @After
    public void closeConnection() {
        dataBase.close();
    }

    @Test
    public void deleteTestSimple() {
        CatManyToOne oneToMany = new CatManyToOne(12,"",new PersonOneToMany(14,"per"));
        CRUD crud = dataBase.getCrud();
        TransactionsManager tr = dataBase.getTransactionManager();
        tr.begin();
        crud.delete(oneToMany);
        //CatManyToOne cat = (CatManyToOne) dataBase.getCrud().find(CatManyToOne.class,12);
        //assertNull(cat);
        tr.rollback();
        CatManyToOne cat1 = (CatManyToOne) dataBase.getCrud().find(CatManyToOne.class,12);
        assertNotNull(cat1);

    }

    @Test
    public void updateTestSimple() {
        String update = "Update";
        PersonOneToMany oneToMany = new PersonOneToMany(14, update);
        CRUD crud = dataBase.getCrud();
        crud.update(oneToMany);
        PersonOneToMany found = (PersonOneToMany) crud.find(PersonOneToMany.class, 14);
        String es = found.getName();
        assertEquals(update.length(), es.length());
        assertEquals(update, es);
    }

    @Test
    public void updateTestManyToOne() {
        CatManyToOne oneToMany = new CatManyToOne(12, "new Name", new PersonOneToMany(14, ""));
        CRUD crud = dataBase.getCrud();
        crud.update(oneToMany);
        CatManyToOne found = (CatManyToOne) crud.find(CatManyToOne.class, 12);
        logger.info(found);
        assertEquals(found.getName(),"new Name");
    }
}
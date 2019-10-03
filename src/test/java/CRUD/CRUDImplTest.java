package CRUD;

import connection.DataBaseImplementation;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import tablecreation.classesintesting.*;

import java.util.HashSet;
import java.util.Set;

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
    public void saveCatOTOMWithoutAI() {
        PersonOneToMany pers = new PersonOneToMany();
        pers.setId(14);
        pers.setName("TestPers");
        //dataBase.getCrud().save(pers);

        CatManyToOne cat = new CatManyToOne();
        cat.setId(12);
        cat.setName("test cat otm");
        cat.setPerson(pers);
        dataBase.getCrud().save(cat);
    }


    // NOT TODO это дичь
//    @Test
//    public void savePersonOTMWithoutAI() {
//        PersonOneToMany pers = new PersonOneToMany();
//        pers.setId(15);
//        pers.setName("TestPers");
//
//        CatManyToOne cat = new CatManyToOne();
//        cat.setId(12);
//        cat.setName("test cat otm");
//        cat.setPerson(pers);
//
//        CatManyToOne cat2 = new CatManyToOne();
//        cat2.setId(13);
//        cat2.setName("test 2 cat otm");
//        cat2.setPerson(pers);
//
//        CatManyToOne cat3 = new CatManyToOne();
//        cat3.setId(14);
//        cat3.setName("test 3 cat otm");
//        cat3.setPerson(pers);
//
//        Set<CatManyToOne> cats = new HashSet<>();
//        cats.add(cat);
//        cats.add(cat2);
//        cats.add(cat3);
//
//        pers.setCats(cats);
//        dataBase.getCrud().save(pers);
//    }

    @Test
    public void saveMTMWithoutAI() {
        StudentMTM sud1 = new StudentMTM();
        sud1.setStudentId(1);
        sud1.setStudentName("STUD 1");

        StudentMTM stud2 = new StudentMTM();
        stud2.setStudentId(2);
        stud2.setStudentName("STUD 2");

        StudentMTM stud3 = new StudentMTM();
        stud3.setStudentId(3);
        stud3.setStudentName("Stud 3");

        CourseMTM course1 = new CourseMTM();
        course1.setCourseId(1);
        course1.setCourseName("MATH_TEST");

        CourseMTM course2 = new CourseMTM();
        course2.setCourseId(2);
        course2.setCourseName("ENGLISH_TEST");

        Set<CourseMTM> courses = new HashSet<>();
        courses.add(course1);
        courses.add(course2);
        sud1.setCourses(courses);

        dataBase.getCrud().save(sud1);
//        Set<StudentMTM> students = new HashSet<>();
//        students.add(sud1);
//        students.add(stud2);
//        students.add(stud3);
//        course1.setStudents(students);
//        dataBase.getCrud().save(course1);
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
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

    @Test
    public void findWithManyToMany(){
        PersonTestManyToMany person  = new PersonTestManyToMany(1,"Kate",20);
        person =(PersonTestManyToMany) dataBase.getCrud().find(person.getClass(),1);
        System.out.println(person);
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
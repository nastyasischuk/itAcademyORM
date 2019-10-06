package test;


import CRUD.aspects.ManyToManyAspect;
import connection.DataBase;
import connection.DataBaseImplementation;
import org.apache.log4j.Logger;

public class TestManyToManyFetchingFromDbLazyInitialization {
    private static Logger logger = Logger.getLogger(TestManyToManyFetchingFromDbLazyInitialization.class);
    static DataBase db = new DataBaseImplementation("D:\\my files\\softserve\\itAcademyORM\\src\\main\\resources\\config.xml",false);
    public static void main(String[] args) {
        db.openConnection();
        ManyToManyAspect.setDb(db);
        //testcatspeople();
        testSeveralManyToManyInOneClass();

    }
    public static void  testcatspeople(){
        ManyToManyAspect.setDb(db);
        CatTestManyToMany catTestManyToMany =(CatTestManyToMany) db.getCrud().find(CatTestManyToMany.class,3);

        System.out.println(catTestManyToMany.getPerson());
        PersonTestManyToMany personTestManyToMany =null;
        for(PersonTestManyToMany person : catTestManyToMany.getPerson())
            personTestManyToMany = person;

        logger.info("Is two classes linked"+personTestManyToMany.cats.contains(catTestManyToMany));
          System.out.println(personTestManyToMany.getCats());
    }
    public static void  testSeveralManyToManyInOneClass(){
        Employee employee =(Employee) db.getCrud().find(Employee.class,2);
        logger.info("Collection without calling get");
        logger.info(employee.clients);
        logger.info(employee.projects );
        logger.info("Collection after calling get");
        logger.info(employee.getClients());
        logger.info(employee.getProjects());
    }
}

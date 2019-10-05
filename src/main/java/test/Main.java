package test;


import CRUDdb.aspects.ManyToManyAspect;
import connection.DataBase;
import connection.DataBaseImplementation;

public class Main {
    public static void main(String[] args) {
        DataBase db = new DataBaseImplementation("D:\\my files\\softserve\\itAcademyORM\\src\\main\\resources\\config.xml",false);
        db.openConnection();
        ManyToManyAspect.setDb(db);
       CatTestManyToMany catTestManyToMany =(CatTestManyToMany) db.getCrud().find(CatTestManyToMany.class,3);

        System.out.println(catTestManyToMany.getPerson());
        System.out.println(catTestManyToMany.getPerson());
        PersonTestManyToMany personTestManyToMany =null;
        for(PersonTestManyToMany person : catTestManyToMany.getPerson())
        personTestManyToMany = person;


        System.out.println("With Cts");
        System.out.println(personTestManyToMany.cats.contains(catTestManyToMany));
        System.out.println(personTestManyToMany.getCats());
    }
}

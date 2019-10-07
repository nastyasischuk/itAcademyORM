package tablecreation.classesintesting;

import annotations.Column;
import annotations.Entity;
import annotations.PrimaryKey;
import annotations.Table;
import java.sql.*;

@Entity
@Table(name="person")
public class PersonWithSimpleProperColumns {
    @Column
    @PrimaryKey
    public int id;
    @Column
    private String name;
    @Column
    private int age;
    @Column
    Date bd;
    public PersonWithSimpleProperColumns(){

    }
    public PersonWithSimpleProperColumns(int id, String name, int age,Date date) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.bd = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}

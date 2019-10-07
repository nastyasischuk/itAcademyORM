package tablecreation.classesintesting;

import annotations.*;
import tablecreation.SQLTypes;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "person")
public class Person {

    @PrimaryKey
    private int id_person;

    @Column
    private double salary;

    public Person(int id_person, double salary) {
        this.id_person = id_person;
        this.salary = salary;
    }

    public Person() {
    }

    public int getId_person() {
        return id_person;
    }

    public void setId_person(int id_person) {
        this.id_person = id_person;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id_person=" + id_person +
                ", salary=" + salary +
                '}';
    }
}
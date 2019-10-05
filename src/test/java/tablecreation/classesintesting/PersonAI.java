package tablecreation.classesintesting;

import annotations.Column;
import annotations.Entity;
import annotations.PrimaryKey;
import annotations.Table;

@Entity
@Table(name = "personAI")
public class PersonAI {

    @PrimaryKey
    @Column(name = "personAI_id", autoincrement = true)
    private int id_person;

    @Column(name = "salaryAI")
    private double salary;

    public PersonAI(int id_person, double salary) {
        this.id_person = id_person;
        this.salary = salary;
    }

    public PersonAI() {
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
}

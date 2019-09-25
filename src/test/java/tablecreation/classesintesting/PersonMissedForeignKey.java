package tablecreation.classesintesting;

import annotations.Column;
import annotations.Entity;

@Entity
public class PersonMissedForeignKey {
    @Column
    private int id;
    @Column
    private String name;
    @Column
    private int age;
    @Column
    private PersonWithSimpleProperColumns per;

    public PersonMissedForeignKey(int id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
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

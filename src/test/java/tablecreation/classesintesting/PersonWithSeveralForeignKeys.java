package tablecreation.classesintesting;

import annotations.*;

@Entity
public class PersonWithSeveralForeignKeys {
    @Column(name ="p_id")
    @PrimaryKey
    public int id;
    @Column
    private String name;
    @Column
    private int age;

    @ForeignKey(name = "per_constr")
    PersonWithConstraints person1;
    @ForeignKey
    PersonWithSimpleProperColumns person2;

    public PersonWithSeveralForeignKeys(int id, String name, int age) {
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

    public void setPerson1(PersonWithConstraints person1) {
        this.person1 = person1;
    }

    public void setPerson2(PersonWithSimpleProperColumns person2) {
        this.person2 = person2;
    }
}

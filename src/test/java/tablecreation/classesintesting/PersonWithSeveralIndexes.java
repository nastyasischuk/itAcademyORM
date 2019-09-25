package tablecreation.classesintesting;

import annotations.*;

@Entity
public class PersonWithSeveralIndexes {
    @Column
    @PrimaryKey
    public int id;

    @Column
    @Index(name="name_index")
    private String first_name;

    @Column
    @Index(name="name_index")
    private String last_name;

    @Column
    private int age;

    public PersonWithSeveralIndexes(int id, String first_name, int age) {
        this.id = id;
        this.first_name = first_name;
        this.age = age;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}

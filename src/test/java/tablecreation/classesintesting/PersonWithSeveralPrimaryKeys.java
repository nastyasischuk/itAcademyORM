package tablecreation.classesintesting;

import annotations.Column;
import annotations.Entity;
import annotations.PrimaryKey;
import annotations.Table;

@Entity
@Table(name="person")
public class PersonWithSeveralPrimaryKeys {
    @Column(name ="p_id")
    @PrimaryKey
    public int id;
    @Column
    private String name;
    @Column
    @PrimaryKey
    private int age;

    public PersonWithSeveralPrimaryKeys(int id, String name, int age) {
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

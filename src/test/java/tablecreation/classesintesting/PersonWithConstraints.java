package tablecreation.classesintesting;

import annotations.*;

@Entity
@Check("age>18")
public class PersonWithConstraints {
    @Column
    @PrimaryKey(autoincrement = true)
    public int id;
    @Column(unique = true)
    private String name;
    @Column
    @Default("18")
    private int age;
    @Column
    @NotNull
    private String email;

    public PersonWithConstraints(int id, String name, int age) {
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

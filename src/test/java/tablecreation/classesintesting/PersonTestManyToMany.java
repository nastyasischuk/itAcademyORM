package tablecreation.classesintesting;

import annotations.*;

import java.util.Collection;

public class PersonTestManyToMany {
    @PrimaryKey
    private int id;
    @Column
    private String name;
    @Column
    private int age;
    @ManyToMany(mappedBy = "person")
            @AssociatedTable(associatedTableName = "person_cat",
            joinColumns =@ForeignKey(name = "p_id"),
            inverseJoinColumns = @ForeignKey(name = "c_id"))
    Collection<CatTestManyToMany> cats;

    public PersonTestManyToMany(int id, String name, int age) {
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

    public Collection<CatTestManyToMany> getCats() {
        return cats;
    }

    public void setCats(Collection<CatTestManyToMany> cats) {
        this.cats = cats;
    }
}

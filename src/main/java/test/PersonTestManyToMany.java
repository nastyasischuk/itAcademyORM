package test;

import annotations.*;

import java.util.Collection;

@Table(name="person")
@Entity
public class PersonTestManyToMany {
    @PrimaryKey
    @Column
    private int id;
    @Column
    private String name;

    @ManyToMany(mappedBy = "person")
            @AssociatedTable(associatedTableName = "cat_person",
            joinColumns =@ForeignKey(name = "p_id"),
            inverseJoinColumns = @ForeignKey(name = "c_id"))
    Collection<CatTestManyToMany> cats;

    public PersonTestManyToMany() {
    }

    public PersonTestManyToMany(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public PersonTestManyToMany setId(int id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<CatTestManyToMany> getCats() {
        return cats;
    }

    public void setCats(Collection<CatTestManyToMany> cats) {
        this.cats = cats;
    }

    @Override
    public String toString() {
        return "PersonTestManyToMany{" +
                "id=" + id +
                ", name='" + name + '\'' ;
    }
}

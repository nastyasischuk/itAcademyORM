package test;

import annotations.*;

import java.util.Collection;
@Table(name="cat")
@Entity
public class CatTestManyToMany {
    @PrimaryKey
    private int id;
    @Column
    private String name;
    @ManyToMany(mappedBy = "cats")
    Collection<PersonTestManyToMany> person;

    public CatTestManyToMany() {
    }

    public CatTestManyToMany(int id, String nameCat) {
        this.id = id;
        this.name = nameCat;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNameCat() {
        return name;
    }

    public void setNameCat(String nameCat) {
        this.name = nameCat;
    }

    public Collection<PersonTestManyToMany> getPerson() {
        return person;
    }

    public void setPerson(Collection<PersonTestManyToMany> person) {
        this.person = person;
    }

    @Override
    public String toString() {
        return "CatTestManyToMany{" +
                "id=" + id +
                ", nameCat='" + name + "\'}]";
    }
}

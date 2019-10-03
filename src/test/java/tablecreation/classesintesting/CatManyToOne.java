package tablecreation.classesintesting;

import annotations.*;

import java.util.Objects;

@Entity
@Table(name="catm")
public class CatManyToOne {

    @Column
    @PrimaryKey
    private int id;
    @Column
    private String name;

    @ManyToOne
    private PersonOneToMany person;

    public CatManyToOne(int id, String name, PersonOneToMany person) {
        this.id = id;
        this.name = name;
        this.person = person;
    }

    public CatManyToOne() {
    }

    @Override
    public String toString() {
        return "CatManyToOne{" +
                "id=" + id +
                ", name='" + name + '\'' +
//                ", person=" + person.getName() +"id "+person.getId()+
                '}';
    }

    public PersonOneToMany getPerson() {
        return person;
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

    public void setPerson(PersonOneToMany person) {
        this.person = person;
    }
}

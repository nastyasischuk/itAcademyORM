package tablecreation.classesintesting;

import annotations.*;

import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "person")
public class PersonOneToMany {
    @Column
    @PrimaryKey
    private int id;
    @Column
    private String name;

    @OneToMany(mappedBy = "person",typeOfReferencedObject = CatManyToOne.class)
    private Collection<CatManyToOne> cats;

    public PersonOneToMany(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public PersonOneToMany() {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PersonOneToMany that = (PersonOneToMany) o;
        return id == that.id &&
                name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "PersonOneToMany{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", cats=" + cats +
                '}';
    }
    public Collection<CatManyToOne> getCollectrion(){
        return cats;
    }

    public void setCats(Collection<CatManyToOne> cats) {
        this.cats = cats;
    }
}

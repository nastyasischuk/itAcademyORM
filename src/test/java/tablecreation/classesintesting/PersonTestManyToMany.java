package tablecreation.classesintesting;

import annotations.*;

import java.util.Collection;
@Entity
@Table(name ="person")
public class PersonTestManyToMany {
    @PrimaryKey

    private int id;
    @Column
    private String name;
    @ManyToMany(mappedBy = "person",typeOfReferencedObject = CatTestManyToMany.class)
            @AssociatedTable(associatedTableName = "cat_person",
            joinColumns =@ForeignKey(name = "p_id"),
            inverseJoinColumns = @ForeignKey(name = "c_id"))
    Collection<CatTestManyToMany> cats;

    public PersonTestManyToMany(int id, String name, int age) {
        this.id = id;
        this.name = name;
    }
    public PersonTestManyToMany(){

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
                ", name='" + name + '\'' +
                ", cats=" + cats +
                '}';
    }
}

package tablecreation.classesintesting;

import annotations.Column;
import annotations.ManyToMany;
import annotations.PrimaryKey;
import annotations.Table;

import java.util.Collection;
@Table(name ="cat")
public class CatTestManyToMany {
    @PrimaryKey
    private int id;
    @Column(name = "name")
    private String nameCat;
    @ManyToMany(mappedBy = "",typeOfReferencedObject = PersonTestManyToMany.class)
    Collection<PersonTestManyToMany> person;
    public CatTestManyToMany(){

    }
    public CatTestManyToMany(int id, String nameCat) {
        this.id = id;
        this.nameCat = nameCat;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNameCat() {
        return nameCat;
    }

    public void setNameCat(String nameCat) {
        this.nameCat = nameCat;
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
                ", nameCat='" + nameCat + '\'';

    }
}

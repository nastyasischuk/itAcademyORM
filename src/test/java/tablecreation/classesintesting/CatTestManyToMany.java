package tablecreation.classesintesting;

import annotations.Column;
import annotations.ManyToMany;
import annotations.PrimaryKey;

import java.util.Collection;

public class CatTestManyToMany {
    @PrimaryKey
    private int id;
    @Column
    private String nameCat;
    @ManyToMany(mappedBy = "")
    Collection<PersonTestManyToMany> person;

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
}

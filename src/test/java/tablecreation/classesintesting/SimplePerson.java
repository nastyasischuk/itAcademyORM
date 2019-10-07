package tablecreation.classesintesting;

import annotations.Column;
import annotations.Entity;
import annotations.PrimaryKey;
import annotations.Table;

import java.util.Objects;

@Table(name = "person")
@Entity
public class SimplePerson {
    @Column
    @PrimaryKey
    private int id;
    @Column
    private String name;

    public SimplePerson(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public SimplePerson() {
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
    public String toString() {
        return "SimplePerson{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SimplePerson that = (SimplePerson) o;
        return id == that.id &&
                name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }


}

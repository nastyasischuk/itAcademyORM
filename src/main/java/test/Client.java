package test;

import annotations.Column;
import annotations.Entity;
import annotations.ManyToMany;
import annotations.PrimaryKey;

import java.util.Collection;

@Entity
public class Client {
    @PrimaryKey
    private int id;
    @Column
    private String name;
    @ManyToMany(mappedBy = "clients")
    Collection<Employee> employees;

    public Client() {
    }

    public Client(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Collection<Employee> getEmployees() {
        return employees;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}

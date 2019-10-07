package test;

import annotations.Column;
import annotations.Entity;
import annotations.ManyToMany;
import annotations.PrimaryKey;

import java.util.Collection;

@Entity
public class Project {
    @PrimaryKey
    private int id;
    @Column
    private String name;
    @ManyToMany(mappedBy = "projects")
    Collection<Employee> employees;

    public Project() {
    }

    public Project(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Collection<Employee> getEmployees() {
        return employees;
    }

    @Override
    public String toString() {
        return "Project{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}

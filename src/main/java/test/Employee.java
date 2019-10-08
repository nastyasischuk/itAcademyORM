package test;

import annotations.*;

import java.util.Collection;

@Entity
public class Employee {
    @PrimaryKey
    private int id;
    @Column
    private int age;
    @ManyToMany
    @AssociatedTable(associatedTableName = "employee_project",
    joinColumns = @ForeignKey(name = "em_id"),
    inverseJoinColumns = @ForeignKey(name ="pr_id"))
    Collection<Project> projects;
    @ManyToMany
    @AssociatedTable(associatedTableName = "employee_client",
            joinColumns = @ForeignKey(name = "em_id"),
            inverseJoinColumns = @ForeignKey(name ="cl_id"))
    Collection<Client> clients;

    public Employee() {
    }

    public Employee(int id, int age) {
        this.id = id;
        this.age = age;
    }

    public int getId() {
        return id;
    }

    public int getAge() {
        return age;
    }

    public Collection<Project> getProjects() {
        return projects;
    }

    public Collection<Client> getClients() {
        return clients;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", age=" + age +
                '}';
    }
}

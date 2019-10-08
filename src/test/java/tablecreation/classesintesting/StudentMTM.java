package tablecreation.classesintesting;

import annotations.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "STUDENT")
public class StudentMTM {

    @PrimaryKey
    @Column(name = "STUDENT_ID")
    private Integer studentId;

    @Column(name = "STUDENT_NAME")
    private String studentName;

    @ManyToMany
    @AssociatedTable(associatedTableName = "STUDENT_COURSE", joinColumns = @ForeignKey(name = "STUDENT_ID"),
            inverseJoinColumns = @ForeignKey(name = "COURSE_ID"))
    private Set<CourseMTM> courses = new HashSet<CourseMTM>(0);

    public long getStudentId() {
        return this.studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return this.studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public Set<CourseMTM> getCourses() {
        return this.courses;
    }

    public void setCourses(Set<CourseMTM> courses) {
        this.courses = courses;
    }

    public StudentMTM() {
    }

    public StudentMTM(String studentName) {
        this.studentName = studentName;
    }

    public StudentMTM(String studentName, Set<CourseMTM> courses) {
        this.studentName = studentName;
        this.courses = courses;
    }

    @Override
    public String toString() {
        return "StudentMTM{" +
                "studentId=" + studentId +
                ", studentName='" + studentName + '\'' +
                ", courses=" + courses +
                '}';
    }
}

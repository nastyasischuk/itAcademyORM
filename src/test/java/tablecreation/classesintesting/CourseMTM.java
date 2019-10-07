package tablecreation.classesintesting;


import annotations.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="COURSE")
public class CourseMTM {

    @PrimaryKey
    @Column(name="COURSE_ID")
    private long courseId;

    @Column(name="COURSE_NAME")
    private String courseName;

    @ManyToMany(mappedBy = "courses")
    private Set<StudentMTM> students = new HashSet<>();

    public CourseMTM() {
    }

    public CourseMTM(String courseName) {
        this.courseName = courseName;
    }


    public long getCourseId() {
        return this.courseId;
    }

    public void setCourseId(long courseId) {
        this.courseId = courseId;
    }


    public String getCourseName() {
        return this.courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public Set<StudentMTM> getStudents() {
        return students;
    }

    public void setStudents(Set<StudentMTM> students) {
        this.students = students;
    }

    @Override
    public String toString() {
        return "CourseMTM{" +
                "courseId=" + courseId +
                ", courseName='" + courseName + '\'' +
                ", students=" + students +
                '}';
    }
}

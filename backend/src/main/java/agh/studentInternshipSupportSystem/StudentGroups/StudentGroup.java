package agh.studentInternshipSupportSystem.StudentGroups;

import agh.studentInternshipSupportSystem.Students.Student;
import agh.studentInternshipSupportSystem.UniversitySupervisors.UniversitySupervisor;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "student_groups")
@Getter @Setter @NoArgsConstructor
public class StudentGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String studentGroupName;

    @ManyToOne
    @JoinColumn(name = "us_id")
    private UniversitySupervisor universitySupervisor;

    @OneToMany(mappedBy = "studentGroup")
    @JsonIgnore
    private Set<Student> students;

    public StudentGroup(String studentGroupName, UniversitySupervisor universitySupervisor) {
        this.studentGroupName = studentGroupName;
        this.studentGroupName = studentGroupName;
        this.universitySupervisor = universitySupervisor;
        this.students = new HashSet<>();
    }
}

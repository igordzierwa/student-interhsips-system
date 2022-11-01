package agh.studentInternshipSupportSystem.Students;

import agh.studentInternshipSupportSystem.Departments.Department;
import agh.studentInternshipSupportSystem.StudentGroups.StudentGroup;
import agh.studentInternshipSupportSystem.Students.RequestBody.RegisterStudent;
import agh.studentInternshipSupportSystem.UniversitySupervisors.UniversitySupervisor;
import agh.studentInternshipSupportSystem.User.User;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;

@Entity
@Data
@Table(name = "students")
@Getter @Setter @NoArgsConstructor
public class Student extends User {
    @OneToOne
    private Department department;

    @OneToOne
    private UniversitySupervisor universitySupervisor;

    @ManyToOne
    @JoinColumn(name = "student_group_id")
    private StudentGroup studentGroup;

    public Student(RegisterStudent registerStudent) {
        this.setUsername(registerStudent.getUsername());
        this.setName(registerStudent.getName());
        this.setSurname(registerStudent.getSurname());
        this.setEmail(registerStudent.getEmail());
        this.setPassword(registerStudent.getPassword());
    }
}

package agh.studentInternshipSupportSystem.StudentGroups;

import agh.studentInternshipSupportSystem.StudentGroups.RequestBody.AddStudentGroup;

public interface StudentGroupService {
    StudentGroup findById(Long id);
    void save(StudentGroup studentGroup);
    void edit(AddStudentGroup addStudentGroup, Long id);
}

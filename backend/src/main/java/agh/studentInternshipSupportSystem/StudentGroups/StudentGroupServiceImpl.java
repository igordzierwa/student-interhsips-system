package agh.studentInternshipSupportSystem.StudentGroups;

import agh.studentInternshipSupportSystem.Exceptions.RecordNotFoundException;
import agh.studentInternshipSupportSystem.StudentGroups.RequestBody.AddStudentGroup;
import agh.studentInternshipSupportSystem.UniversitySupervisors.UniversitySupervisorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentGroupServiceImpl implements StudentGroupService {
    @Autowired
    private StudentGroupRepository studentGroupRepository;

    @Autowired
    private UniversitySupervisorRepository universitySupervisorRepository;

    @Override
    public StudentGroup findById(Long id) {
        StudentGroup existStudentGroup = studentGroupRepository
                .findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Student Group Not Exist"));
        return existStudentGroup;
    }

    @Override
    public void save(StudentGroup studentGroup) {
        studentGroupRepository.save(studentGroup);
    }

    @Override
    public void edit(AddStudentGroup addStudentGroup, Long id) {
        StudentGroup existStudentGroup = findById(id);
        existStudentGroup.setUniversitySupervisor(universitySupervisorRepository.findById(id).get());
        existStudentGroup.setStudentGroupName(addStudentGroup.getStudentGroupName());

        studentGroupRepository.save(existStudentGroup);
    }
}

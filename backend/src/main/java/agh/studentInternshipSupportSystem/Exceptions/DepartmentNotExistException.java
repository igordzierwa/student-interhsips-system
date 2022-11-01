package agh.studentInternshipSupportSystem.Exceptions;

public class DepartmentNotExistException extends RuntimeException {
    public DepartmentNotExistException(String txt){
        super(txt);
    }
}

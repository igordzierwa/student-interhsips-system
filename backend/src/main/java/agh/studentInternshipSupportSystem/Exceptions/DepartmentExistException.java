package agh.studentInternshipSupportSystem.Exceptions;

public class DepartmentExistException extends RuntimeException {
    public DepartmentExistException(String txt){
        super(txt);
    }
}

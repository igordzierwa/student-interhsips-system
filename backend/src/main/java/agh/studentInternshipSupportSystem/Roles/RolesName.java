package agh.studentInternshipSupportSystem.Roles;

import lombok.Getter;

@Getter
public enum RolesName {
    ADMIN("admin"),
    STUDENT("student"),
    COMPANY_SUPERVISOR("company_supervisor"),
    UNIVERSITY_SUPERVISOR("university_supervisor");

    private String roleName;

    RolesName(String roleName) {
        this.roleName = roleName;
    }
}

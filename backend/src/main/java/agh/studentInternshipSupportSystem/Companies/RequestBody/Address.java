package agh.studentInternshipSupportSystem.Companies.RequestBody;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.NotNull;

@Embeddable
@Getter @Setter @NoArgsConstructor
public class Address {
    @NotNull(message = "City is mandatory")
    private String city;

    @NotNull(message = "Street is mandatory")
    private String street;

    @NotNull(message = "Zip code is mandatory")
    private String zipCode;
}

package sharedrmi.application.dto;

import lombok.Builder;
import lombok.Getter;
import sharedrmi.domain.valueobjects.Address;

import java.io.Serializable;

@Getter
public class CustomerDTO implements Serializable {

    private final String firstName;
    private final String lastName;
    private final String email;
    private final Address address;

    @Builder
    public CustomerDTO(String firstName, String lastName, String email, Address address) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.address = address;
    }
}

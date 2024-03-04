package sharedrmi.domain.valueobjects;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class CustomerData implements Serializable {

    private String firstName;
    private String lastName;
    private String email;
    private Address address;


    protected CustomerData() {
        this.firstName = "";
        this.lastName = "";
        this.email = "";
        this.address = new Address();
    }

    @Builder
    public CustomerData(String firstName, String lastName, String email, Address address) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.address = address;
    }
}

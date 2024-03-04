package sharedrmi.domain.valueobjects;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class Address implements Serializable {

    private String addressCountry;
    private String addressLocality;
    private String postalCode;
    private String streetAddress;


    protected Address() {
        String addressCountry = "";
        String addressLocality = "";
        String postalCode = "";
        String streetAddress = "";
    }

    @Builder
    public Address(String addressCountry, String addressLocality, String postalCode, String streetAddress) {
        this.addressCountry = addressCountry;
        this.addressLocality = addressLocality;
        this.postalCode = postalCode;
        this.streetAddress = streetAddress;
    }
}

package sharedrmi.application.dto;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class UserDataDTO implements Serializable {

    private String emailAddress;
    private String password;

    public UserDataDTO(){}

    @Builder
    public UserDataDTO(String emailAddress, String password) {
        this.emailAddress = emailAddress;
        this.password = password;
    }
}

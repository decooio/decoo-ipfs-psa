package com.decoo.psa.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class UserCreateDTO {

    @NotBlank(message = "nick name can not empty")
    @Length(min = 1, max = 64, message = "nick name length between 1 to 64")
    private String nickName;

    @NotBlank(message = "email name can not empty")
    @Email(message = "email pattern invalid")
    private String email;

    @NotBlank(message = "password can not empty")
    @Length(min = 6, max = 16, message = "password length between 6 to 16")
    private String password;

    private Integer thirdParty;

    private String host;
}

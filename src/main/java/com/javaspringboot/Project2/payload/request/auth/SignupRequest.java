package com.javaspringboot.Project2.payload.request.auth;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.util.Date;
import java.util.Set;

@Data
@Getter
@Setter
public class SignupRequest {
    @NotBlank
    @Size(min = 6,max = 20)
    private String username;
    @NotBlank
    @Email
    @Size(max = 50)
    private String email;

    private Set<String> role;
    @NotEmpty
    @Size(max = 120)
    private String password;
    @NotBlank
    private String fullname;
    @NotEmpty
    private String phone;
    @NotBlank
    private String diaChi;
    @NotNull
    private String gioiTinh;


}

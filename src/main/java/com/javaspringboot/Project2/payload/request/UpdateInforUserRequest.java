package com.javaspringboot.Project2.payload.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.Set;

@Data
@Getter
@Setter
public class UpdateInforUserRequest {

    @NotBlank
    private String fullname;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date joinDate;
    @NotEmpty
    private String phone;
    @NotBlank
    private String diaChi;

    private String gioiTinh;

}

package com.javaspringboot.Project2.payload.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.javaspringboot.Project2.enumm.ERole;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Data
@Setter
@Getter
public class UserResponse {

    private Long id;

    private String username;

    private String email;


    private String fullname;

    private String phone;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date joinDate;

    private String diaChi;

    private String gioiTinh;

    private List<ERole> roles;
}

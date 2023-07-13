package com.javaspringboot.Project2.payload.response;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Data
@Setter
@Getter
public class UserInfoToBillResponse {

    private Long id;

    private String username;

    private String fullname;

    private String phone;

}

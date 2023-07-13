package com.javaspringboot.Project2.payload.response;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Setter
@Getter
public class SupplierResponse {

    private long id;

    private String name;

    private String phone;

    private String diaChi;

}

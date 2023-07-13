package com.javaspringboot.Project2.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class NhapHangBillDetailDTO {

    private  Long goods_id;

    private int count;

    private int price;
}

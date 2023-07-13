package com.javaspringboot.Project2.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class GoodsDTO {

    private  String name;

    private int count;

    private int price;

    private String image;

    private String goodsType_name;

    private Long supplier_id;

}

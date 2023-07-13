package com.javaspringboot.Project2.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Data
@Getter
@Setter
public class BanHangBillDTO {

    private Long ban_id;

    private CustomerDTO customer;

    private Set<BanHangBillDetailDTO> banHangBillDetail;
}

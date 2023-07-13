package com.javaspringboot.Project2.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Set;

@Data
@Getter
@Setter
public class NhapHangBillDTO {

    private Set<NhapHangBillDetailDTO> nhapHangBillDetail;
}

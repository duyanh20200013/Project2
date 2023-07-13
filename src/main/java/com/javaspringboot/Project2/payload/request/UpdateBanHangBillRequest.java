package com.javaspringboot.Project2.payload.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.javaspringboot.Project2.dto.BanHangBillDetailDTO;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.Set;

@Data
@Getter
@Setter
public class UpdateBanHangBillRequest {
    private Long id;

    private Set<BanHangBillDetailDTO> banHangBillDetail;

}

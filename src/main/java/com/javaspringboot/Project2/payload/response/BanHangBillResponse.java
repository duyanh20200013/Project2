package com.javaspringboot.Project2.payload.response;

import com.javaspringboot.Project2.dto.BanDTO;
import com.javaspringboot.Project2.dto.CustomerDTO;
import com.javaspringboot.Project2.enumm.EStatusBill;
import com.javaspringboot.Project2.model.Ban;
import com.javaspringboot.Project2.model.BanHangBillDetail;
import com.javaspringboot.Project2.model.NhapHangBillDetail;
import com.javaspringboot.Project2.model.User;
import lombok.*;

import java.util.Date;
import java.util.Set;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BanHangBillResponse {

    private long id;

    private Date exportTime;

    private int tongTien;

    private int diemTichLuy;

    private int giamGia;

    private EStatusBill status;

    private Ban ban;

    private CustomerResponse customer;

    private UserInfoToBillResponse user;

    private Set<BanHangBillDetail> banHangBillDetails;

}

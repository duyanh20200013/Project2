package com.javaspringboot.Project2.payload.response;

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
public class NhapHangBillResponse {

    private long id;

    private Date importTime;

    private int tongTien;

    private UserInfoToBillResponse user;

    private Set<NhapHangBillDetail> nhapHangBillDetails;

}

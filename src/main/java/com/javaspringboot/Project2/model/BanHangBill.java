package com.javaspringboot.Project2.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.javaspringboot.Project2.enumm.EStatusBill;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Set;


@Entity
@Table(name = "banHangBills")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BanHangBill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "banHangBill_id")
    private Long id;

    @NotNull
    private Date exportTime;

    @NotNull
    private int tongTien;

    @NotNull
    private int diemTichLuy;

    private int giamGia;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private EStatusBill status;

    @ManyToOne
    @JoinColumn(name = "ban_id")
    //@JsonBackReference
    private Ban ban;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    @JsonBackReference
    private Customer customer;

    @ManyToOne(fetch =  FetchType.EAGER)
    @JoinColumn(name = "nhanvien_id")
    @JsonBackReference
    private User user;

    @OneToMany(mappedBy = "banHangBill",cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<BanHangBillDetail> banHangBillDetails;

    public BanHangBill(Date exportTime, int giamGia, EStatusBill status, Ban ban) {
        this.exportTime = exportTime;
        this.giamGia = giamGia;
        this.status = status;
        this.ban = ban;
    }
}

package com.javaspringboot.Project2.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Entity
@Table(name = "nhapHangBillDetails")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NhapHangBillDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "nhapHangBillDetail_id")
    private Long id;

    @NotNull
    private int count;

    @NotNull
    private int price;

    @ManyToOne
    @JoinColumn(name = "nhapHangBill_id")
    @JsonBackReference
    private NhapHangBill nhapHangBill;

    @ManyToOne
    @JoinColumn(name = "goods_id")
    //@JsonBackReference
    private Goods goods;

    public NhapHangBillDetail(int count, int price, Goods goods) {
        this.count = count;
        this.price = price;
        this.goods = goods;
    }
}

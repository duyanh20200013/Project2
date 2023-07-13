package com.javaspringboot.Project2.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.javaspringboot.Project2.enumm.EStatusBill;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Entity
@Table(name = "banHangBillDetails")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BanHangBillDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "banHangBillDetail_id")
    private Long id;

    @NotNull
    private int count;

    @NotNull
    private int price;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private EStatusBill status;

    @ManyToOne
    @JoinColumn(name = "banHangBill_id")
    @JsonBackReference
    private BanHangBill banHangBill;

    @ManyToOne
    @JoinColumn(name = "goods_id")
    //@JsonBackReference
    private Goods goods;

    public BanHangBillDetail(int count, int price, Goods goods, EStatusBill status) {
        this.count = count;
        this.price = price;
        this.goods = goods;
        this.status=status;
    }
}

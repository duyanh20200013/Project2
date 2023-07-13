package com.javaspringboot.Project2.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;


@Entity
@Table(name = "goods",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "name"),
        })
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Goods {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "goods_id")
    private Long id;

    @NotBlank
    private String name;

    @NotNull
    private int count;

    @NotNull
    private int price;

    @NotBlank
    private String image;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goodstype_id")
    @JsonBackReference
    private GoodsType goodstype;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id")
    @JsonBackReference
    private Supplier supplier;

    @OneToMany(mappedBy = "goods",cascade = CascadeType.ALL)
    @JsonIgnore
    //@JsonManagedReference
    private Set<BanHangBillDetail> banHangBillDetails;

    @OneToMany(mappedBy = "goods",cascade = CascadeType.ALL)
    @JsonIgnore
    //@JsonManagedReference
    private Set<NhapHangBillDetail> nhapHangBillDetails;

    public Goods(String name, int count, int price, String image,GoodsType goodsType,Supplier supplier){
         this.name=name;
         this.count=count;
         this.price=price;
         this.image=image;
         this.goodstype=goodsType;
         this.supplier=supplier;
    }


}

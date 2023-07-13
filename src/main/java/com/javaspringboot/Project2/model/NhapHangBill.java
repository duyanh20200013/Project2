package com.javaspringboot.Project2.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
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
@Table(name = "nhapHangBills")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NhapHangBill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "nhapHangBill_id")
    private Long id;

    private Date importTime;

    @NotNull
    private int tongTien;

    @ManyToOne(fetch =  FetchType.EAGER)
    @JoinColumn(name = "nhanvien_id")
    @JsonBackReference
    private User user;

    @OneToMany(mappedBy = "nhapHangBill",cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<NhapHangBillDetail> nhapHangBillDetails;

    public NhapHangBill(Date importTime, User user) {
        this.importTime = importTime;
        this.user = user;
    }
}

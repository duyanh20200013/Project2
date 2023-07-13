package com.javaspringboot.Project2.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Set;


@Entity
@Table(name = "customers",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "phone"),
        })
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Customer {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        @Column(name = "customer_id")
        private Long id;

        @NotBlank
        private String name;

        @NotBlank
        private String phone;

        private int diemTichLuy;

        @OneToMany(mappedBy = "customer",cascade = CascadeType.ALL)
        @JsonManagedReference
        private Set<BanHangBill> banHangBills;

        public Customer(String name, String phone, int diemTichLuy) {
                this.name = name;
                this.phone = phone;
                this.diemTichLuy = diemTichLuy;
        }
}

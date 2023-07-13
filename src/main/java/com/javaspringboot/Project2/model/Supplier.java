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
@Table(name = "suppliers",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "phone"),
        })
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Supplier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "supplier_id")
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String phone;

    @NotBlank
    private String diaChi;

    @OneToMany(fetch = FetchType.EAGER,mappedBy = "supplier",cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<Goods> goods;

    public Supplier(String name, String phone, String diaChi) {
        this.name = name;
        this.phone = phone;
        this.diaChi = diaChi;
    }
}

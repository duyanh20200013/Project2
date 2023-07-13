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
@Table(name = "goodstypes",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "name"),
        })
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GoodsType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "goodstype_id")
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    @OneToMany(fetch = FetchType.EAGER,mappedBy = "goodstype",cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<Goods> goods;

    public GoodsType(String name, String description){
        this.name=name;
        this.description=description;
    }

}

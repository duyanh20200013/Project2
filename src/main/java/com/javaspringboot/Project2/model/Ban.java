package com.javaspringboot.Project2.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.javaspringboot.Project2.enumm.EStatusBan;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Set;


@Entity
@Table(name = "bans",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "name"),
        })
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Ban {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ban_id")
    private Long id;

    @NotBlank
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private EStatusBan status;

    @ManyToOne(
            fetch = FetchType.LAZY
    )
    @JoinColumn(name = "area_id")
    @JsonBackReference
    private Area area;

    @OneToMany(mappedBy = "ban",cascade = CascadeType.ALL)
    //@JsonManagedReference
    @JsonIgnore
    private Set<BanHangBill> banHangBills;

    public Ban(String name,EStatusBan status,Area area){
        this.status=status;
        this.name=name;
        this.area=area;
    }


}

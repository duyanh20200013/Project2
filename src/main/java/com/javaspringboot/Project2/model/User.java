package com.javaspringboot.Project2.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "username"),
                @UniqueConstraint(columnNames = "email")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name="id")
    private Long id;

    @NotBlank
    @Size(max = 20)
    private String username;

    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    @NotBlank
    @Size(max = 120)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @NotBlank
    private String fullname;

    @NotBlank
    private String phone;

    private Date joinDate;

    @NotNull
    private String gioiTinh;

    @NotBlank
    private String diaChi;


    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    @JsonManagedReference
    private Set<Role> roles = new HashSet<>();


    public User(String username, String email, String password, String fullname, String phone, String gioiTinh, String diaChi) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.fullname = fullname;
        this.phone = phone;
        this.joinDate = new Date();
        this.gioiTinh = gioiTinh;
        this.diaChi = diaChi;
    }

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "user",cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<BanHangBill> banHangBills;

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "user",cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<NhapHangBill> nhapHangBills;

//    public User(String fullname,Date birthDate, String phone, String tenVien, String tenPhong, String tenBan){
//        this.fullname=fullname;
//        this.birthDate = birthDate;
//        this.phone=phone;
//        this.tenVien=tenVien;
//        this.tenPhong=tenPhong;
//        this.tenBan=tenBan;
//    }
}

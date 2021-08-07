package com.sogyeong.cbcb.defaults.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Table(name = "default_address")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="local_code" , unique = true, nullable = false)
    private long seq;

    @Column(name ="city")
    private String city;

    @Column(name = "district")
    private String district;

    @Column(name = "neighborhood")
    private String neighborhood;

    @Builder
    Address(long seq, String city,String district, String neighborhood){
        this.seq = seq;
        this.city =city;
        this.district=district;
        this.neighborhood=neighborhood;
    }
}

package com.libaitu.libaitu.entity;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="roles")
@Getter
@Setter
@NoArgsConstructor
public class Roles {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer roleId;
    @Enumerated(EnumType.STRING)
    private ERole role;

}

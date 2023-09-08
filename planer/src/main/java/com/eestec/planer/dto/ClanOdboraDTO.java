package com.eestec.planer.dto;

import jakarta.persistence.*;

@Entity
@Table(name="clanodbora")
public class ClanOdboraDTO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdClana")
    private int IdClana;

    @OneToOne
    @JoinColumn(name = "IdClana")
    private SuperUserDTO superuser;

    public ClanOdboraDTO(){}

    public ClanOdboraDTO(SuperUserDTO superUserDTO){this.superuser=superUserDTO;}

    public SuperUserDTO getSuperuser() {
        return superuser;
    }

    public void setSuperuser(SuperUserDTO superuser) {
        this.superuser = superuser;
    }
}

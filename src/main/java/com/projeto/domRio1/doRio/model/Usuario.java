package com.projeto.domRio1.doRio.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "usuario")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    @Column(unique = true)
    private String usuarioLogar;
    private String senha;
    @Enumerated(EnumType.STRING)
    private TipoUsuario tipo;
}

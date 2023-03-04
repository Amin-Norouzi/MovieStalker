package com.aminnorouzi.ms.model.movie;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Setter
@Getter
@Entity
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cache")
public class Cache implements Serializable {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String url;

    @Lob
    private String json;

    private LocalDate expiresAt;

    public static Cache of(String url, String json) {
        return new Cache(null, url, json, null);
    }
}

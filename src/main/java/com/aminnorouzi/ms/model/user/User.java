package com.aminnorouzi.ms.model.user;

import com.aminnorouzi.ms.model.movie.Movie;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Setter
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(unique = true)
    private String username;

    private String password;
    private String fullName;

    @OneToMany(mappedBy = "user")
    @ToString.Exclude
    private List<Movie> movies;
}
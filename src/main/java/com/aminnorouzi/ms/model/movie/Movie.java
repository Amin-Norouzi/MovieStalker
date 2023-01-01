package com.aminnorouzi.ms.model.movie;

import com.aminnorouzi.ms.annotation.*;
import com.aminnorouzi.ms.model.user.User;
import com.fasterxml.jackson.annotation.*;
import lombok.*;
import org.hibernate.Hibernate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonInclude.Include;

@Setter
@Getter
@Entity
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "movie")
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@EntityListeners(AuditingEntityListener.class)
public class Movie {

    @Id
    @JsonIgnore
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "movie_seq")
//    @SequenceGenerator(name = "movie_seq", sequenceName = "movie_seq", allocationSize = 1)
    private Long id;

    @NotNull
    @JsonProperty("id")
    @Column(unique = true)
    private Long tmdbId;

    @NotNull
    @IdFromJson
    @Column(unique = true)
    @JsonProperty("external_ids")
    private String imdbId;

    @JsonAlias({"title", "name"})
    private String title;

    @Column(length = 2055)
    @JsonProperty("overview")
    private String overview;

    @FullPathUrl
    @JsonProperty("poster_path")
    private String poster;

    @FullPathUrl
    @JsonProperty("backdrop_path")
    private String backdrop;

    @JsonAlias({"release_date", "first_air_date"})
    private LocalDate released;

    @JsonIgnore
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Type type;

    @SimpleDoubleNumber
    @JsonProperty("vote_average")
    private Double rating;

    @ArrayFirstValue
    @JsonAlias({"runtime", "episode_run_time"})
    private Integer runtime;

    @JsonProperty(value = "number_of_episodes")
    private Integer episodes = 1;

    @JsonProperty(value = "number_of_seasons")
    private Integer seasons = 0;

    @CollectionOfJson
    @JsonProperty("genres")
    @Column(name = "genre")
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "movie_genres", joinColumns = @JoinColumn(name = "movie_id"))
    private List<String> genres;

    private LocalDateTime watchedAt;

    @CreatedDate
    private LocalDateTime createdAt;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private User user;

    // TODO: generate equals and hashcode methods properly

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Movie movie = (Movie) o;
        return id != null && Objects.equals(id, movie.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
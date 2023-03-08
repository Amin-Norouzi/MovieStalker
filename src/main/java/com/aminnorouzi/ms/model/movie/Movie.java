package com.aminnorouzi.ms.model.movie;

import com.aminnorouzi.ms.annotation.CalculatedTime;
import com.aminnorouzi.ms.annotation.CollectionOfJson;
import com.aminnorouzi.ms.annotation.IdFromJson;
import com.aminnorouzi.ms.annotation.SimpleDoubleNumber;
import com.aminnorouzi.ms.model.user.User;
import com.fasterxml.jackson.annotation.*;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
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
public class Movie implements Serializable {

    @Id
    @JsonIgnore
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @JsonProperty("id")
    private Long tmdbId;

    @NotNull
    @IdFromJson
    @JsonProperty("external_ids")
    private String imdbId;

    @JsonAlias({"title", "name"})
    private String title;

    @ToString.Exclude
    @Column(length = 2055)
    @JsonProperty("overview")
    private String overview;

    @JsonProperty("poster_path")
    private String poster;

    @JsonProperty("backdrop_path")
    private String backdrop;

    @CalculatedTime
    @JsonAlias({"runtime", "episode_run_time"})
    private String runtime;

    @JsonIgnore
    private String website;

    @JsonIgnore
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Type type;

    @SimpleDoubleNumber
    @JsonProperty("vote_average")
    private Double rating;

    @JsonProperty(value = "number_of_episodes")
    private Integer episodes = 1;

    @JsonProperty(value = "number_of_seasons")
    private Integer seasons = 0;

    @CollectionOfJson
    @JsonProperty("genres")
    @Column(name = "genre")
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "movie_genres", joinColumns = {
            @JoinColumn(name = "user_id", referencedColumnName = "user_id"),
            @JoinColumn(name = "movie_id", referencedColumnName = "id")
    })
    private List<String> genres;

    @JsonAlias({"release_date", "first_air_date"})
    private LocalDate released;

    private LocalDateTime watchedAt;
    private LocalDateTime createdAt;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private User user;

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
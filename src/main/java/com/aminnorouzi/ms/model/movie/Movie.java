package com.aminnorouzi.ms.model.movie;

import com.aminnorouzi.ms.annotation.CollectionOfJson;
import com.aminnorouzi.ms.annotation.SimpleDoubleNumber;
import com.aminnorouzi.ms.annotation.ArrayFirstValue;
import com.aminnorouzi.ms.annotation.FullPathUrl;
import com.aminnorouzi.ms.model.user.User;
import com.fasterxml.jackson.annotation.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Setter
@Getter
@Entity
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "movie")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@EntityListeners(AuditingEntityListener.class)
public class Movie {

    @Id
    @JsonIgnore
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonProperty("id")
    private Long tmdbId;

    @JsonProperty("imdb_id")
    private String imdbId;

    @JsonAlias({"title", "name"})
    private String title;

    @Column(length = 2055)
    @JsonProperty("overview")
    private String overview;

    @JsonAlias({"release_date", "first_air_date"})
    private String released;

    @FullPathUrl
    @JsonProperty("poster_path")
    private String poster;

    @FullPathUrl
    @JsonProperty("backdrop_path")
    private String backdrop;

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
    private Integer episodes;

    @JsonProperty("number_of_seasons")
    private Integer seasons;

    @CollectionOfJson
    @JsonProperty("genres")
    @Column(name = "genre_id")
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "movie_genres", joinColumns = @JoinColumn(name = "movie_id"))
    private Set<String> genres;

    private LocalDateTime watchedAt;

    @CreatedDate
    private LocalDate createdAt;

    @JoinColumn(name = "user_id", nullable = false)
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private User user;
}
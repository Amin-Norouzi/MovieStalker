package com.aminnorouzi.ms.model.movie;

import com.aminnorouzi.ms.annotation.CustomNumber;
import com.aminnorouzi.ms.annotation.FirstArrayValue;
import com.aminnorouzi.ms.model.user.User;
import com.fasterxml.jackson.annotation.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.hibernate.validator.constraints.URL;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.NumberFormat;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Setter
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name = "movie")
public class Movie {

    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @NotNull
    @Column(unique = true, updatable = false)
    @JsonProperty("id")
    private Long tmdbId;

    //    @NotBlank
//    @Column(unique = true, updatable = false)
    @JsonProperty("imdb_id")
    private String imdbId;

    @NotBlank
    @JsonAlias({"title", "name"})
    private String title;

    @NotBlank
    @Column(length = 1000)
    @JsonProperty("overview")
    private String overview;

    @NotBlank
    @JsonAlias({"release_date", "first_air_date"})
    private String release;

    @URL
    @NotBlank
    @JsonProperty("poster_path")
    private String poster;

    @URL
    @NotBlank
    @JsonProperty("backdrop_path")
    private String backdrop;

    @JsonIgnore
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Type type;

    @Min(0)
    @Max(10)
    @NotNull
    @CustomNumber
    @PositiveOrZero
    @JsonProperty("vote_average")
    private Double rating;

    @NotNull
    @Positive
    @FirstArrayValue
    @JsonAlias({"runtime", "episode_run_time"})
    private Integer runtime;

    @Positive
    @JsonProperty("number_of_episodes")
    private Integer episodes;

    @Positive
    @JsonProperty("number_of_seasons")
    private Integer seasons;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "movie")
    @JsonProperty("genres")
    private Set<Genre> genres;

    private Boolean isWatched;

    private LocalDate watchedAt;

    @CreatedDate
    @Column(updatable = false)
    private LocalDate createdAt;

    @ManyToOne
    @ToString.Exclude
    @JoinColumn(name = "user_id")
    private User user;

}
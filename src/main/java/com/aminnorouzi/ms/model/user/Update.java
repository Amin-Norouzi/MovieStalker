package com.aminnorouzi.ms.model.user;

import com.aminnorouzi.ms.model.movie.Movie;
import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Update {

    private Long id;
    private String password;
    private String fullName;
    private List<Movie> movies;
}

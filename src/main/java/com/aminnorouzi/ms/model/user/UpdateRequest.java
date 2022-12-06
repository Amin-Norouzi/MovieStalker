package com.aminnorouzi.ms.model.user;

import com.aminnorouzi.ms.model.movie.Movie;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateRequest {

    private Long id;
    private String password;
    private String fullName;
    private List<Movie> movies;
}

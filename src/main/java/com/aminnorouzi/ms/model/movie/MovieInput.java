package com.aminnorouzi.ms.model.movie;

import javafx.scene.shape.Rectangle;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MovieInput {

    private Rectangle rectangle;
    private Movie movie;

    public static MovieInput of(Rectangle rectangle, Movie movie) {
        return new MovieInput(rectangle, movie);
    }
}

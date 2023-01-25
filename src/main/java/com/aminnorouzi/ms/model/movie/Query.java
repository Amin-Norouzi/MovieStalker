package com.aminnorouzi.ms.model.movie;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.validator.routines.UrlValidator;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Query {

    private String title;
    private String released;
    private String imdb;

    public static Query of(String value) {
        if (UrlValidator.getInstance().isValid(value)) {
            return new Query(null, null, value);
        }
        return new Query(value, null, null);
    }
}

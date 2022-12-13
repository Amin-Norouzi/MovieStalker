package com.aminnorouzi.ms.model.movie;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Query {

    private String title;
    private String released;

    public String getFullQuery() {
        return title + " " + released;
    }
}

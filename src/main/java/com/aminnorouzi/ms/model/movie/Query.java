package com.aminnorouzi.ms.model.movie;

import com.aminnorouzi.ms.util.StringUtil;
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
    private Boolean trending;

    public static Query of(String value) {
        if (StringUtil.isUrl(value)) {
            return new Query(null, null, value, false);
        }

        return new Query(value, null, null, false);
    }

    public static Query of(Boolean trending) {
        return new Query(null, null, null, trending);
    }
}

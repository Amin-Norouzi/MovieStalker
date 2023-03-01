package com.aminnorouzi.ms.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class StringUtil {

    @Value("${movie.client.api.imdb.base-url}")
    private String imdbBaseUrl;

    public String generateImdbUrl(String imdbId) {
        return imdbBaseUrl.concat(imdbId);
    }
}

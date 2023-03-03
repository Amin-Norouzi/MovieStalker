package com.aminnorouzi.ms.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class StringUtil {

    private static final Pattern IMDB_ID_PATTERN = Pattern.compile("(tt\\d[0-9]*)");

    @Value("${movie.client.api.imdb.base-url}")
    private String imdbBaseUrl;

    public String generateImdbUrl(String imdbId) {
        return imdbBaseUrl.concat(imdbId);
    }

    public String extractImdbId(String url) {
        Matcher matcher = IMDB_ID_PATTERN.matcher(url);
        if (matcher.find()) {
            return matcher.group();
        }

        return null;
    }

    public String format(String str, Object... args) {
        return String.format(str, args);
    }
}

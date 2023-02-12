package com.aminnorouzi.ms.tool.view;

import com.aminnorouzi.ms.model.user.User;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
public class CacheKey {

    private View view;
    private Integer total;
    private Integer watched;
    private Object input;

    public static CacheKey of(View view, User user, Object input) {
        int total = 0;
        int watched = 0;

        if (user != null) {
            total = user.getMovies().size();
            watched = user.getMovies().stream()
                    .filter(m -> m.getWatchedAt() != null)
                    .toList().size();
        }

        return new CacheKey(view, total, watched, input);
    }
}

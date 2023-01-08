package com.aminnorouzi.ms.model.movie;

import com.aminnorouzi.ms.model.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MovieRequest {

    private User user;
    private Search search;
}

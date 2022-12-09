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
public class Request {

    private User user;
    private Query query;
}

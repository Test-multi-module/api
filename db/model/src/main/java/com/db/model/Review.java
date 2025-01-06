package com.db.model;

import lombok.*;


@Builder
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class Review {

    private Integer id;

    private Integer productId;

    private String timestamp;

    private String userName;

    private Integer rating;

    private String comment;
}

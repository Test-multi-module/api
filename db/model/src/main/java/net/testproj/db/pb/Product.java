package net.testproj.db.pb;

import lombok.*;


@Builder
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class Product {
    private Integer rating;

    private Integer id;

    private String title;

    private Integer  price;

    private String description;

    private String categories;
}

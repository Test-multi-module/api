package net.testproj.api.dtos.filters;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
public class ProductFilter {

    @JsonProperty("title")
    String title;

    @JsonProperty("price")
    Integer price;

    @JsonProperty("category")
    String category;
}

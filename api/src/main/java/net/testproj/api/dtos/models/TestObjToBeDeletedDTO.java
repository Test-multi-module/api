package net.testproj.api.dtos.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
public class TestObjToBeDeletedDTO {//TODO
    Integer rating;

    @JsonProperty("id")
    Integer id;

    @JsonProperty("title")
    String title;

    @JsonProperty("price")
    Integer  price;

    @JsonProperty("description")
    String description;

    @JsonProperty("categories")
    String categories;
}

package net.testproj.api.dtos.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
public class ReviewDTO {
    @JsonProperty("id")
    Integer id;

    @JsonProperty("product_id")
    Integer productId;

    @JsonProperty("timestamp")
    String timestamp;

    @JsonProperty("user_name")
    String userName;

    @JsonProperty("rating")
    Integer rating;

    @JsonProperty("comment")
    String comment;
}
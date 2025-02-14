package rating.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
public class Rating {

    @Id
    private String id;
    @Min(1)
    @Max(10)
    private float rate;
    @Positive
    private int movieId;
}

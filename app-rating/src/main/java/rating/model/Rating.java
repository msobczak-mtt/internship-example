package rating.model;

import lombok.Data;

@Data
public class Rating {

    private Integer id;
    private float rate;
    private int movieId;
}

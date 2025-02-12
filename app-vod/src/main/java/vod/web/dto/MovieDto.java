package vod.web.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

@Data
@Builder
public class MovieDto {

    private int id;
    @Size(min = 2, max = 50)
    private String title;
    @NotNull
    @URL
    private String poster;
    @Positive
    private int directorId;

}

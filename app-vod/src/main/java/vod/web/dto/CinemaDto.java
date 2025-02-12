package vod.web.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CinemaDto {

    private int id;
    private String name;
    private String logo;

}

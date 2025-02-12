package vod.web.dto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import vod.model.Cinema;

@Component
@RequiredArgsConstructor
@Slf4j
public class CinemaMapper {

    public CinemaDto toDto(Cinema cinema) {
        return CinemaDto.builder()
                .id(cinema.getId())
                .name(cinema.getName())
                .logo(cinema.getLogo())
                .build();
    }
}

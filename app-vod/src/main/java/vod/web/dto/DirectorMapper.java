package vod.web.dto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import vod.model.Director;

@Component
@RequiredArgsConstructor
@Slf4j
public class DirectorMapper {

    public Director fromDto(DirectorDto dto){
        Director director = new Director();
        director.setId(dto.getId());
        director.setFirstName(dto.getFirstName());
        director.setLastName(dto.getLastName());
        return director;
    }

    public DirectorDto toDto(Director director){
        return new DirectorDto(director.getId(), director.getFirstName(), director.getLastName());
    }
}

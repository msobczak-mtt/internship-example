package vod.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vod.model.Director;
import vod.service.MovieService;
import vod.web.dto.DirectorDto;
import vod.web.dto.DirectorMapper;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Map;

@RestController
@RequestMapping("directors")
@RequiredArgsConstructor
@Slf4j
public class DirectorController {
    private final MovieService movieService;
    private final DirectorMapper directorMapper;

    @GetMapping
    public List<DirectorDto> getDirectors() {
        return movieService.getAllDirectors().stream()
                .map(directorMapper::toDto)
                .collect(Collectors.toList());
    }

    @PostMapping
    public ResponseEntity<DirectorDto> addDirector(@RequestBody @Validated DirectorDto directorDto) {
        log.info("About to add director: {}", directorDto);
        Director director = movieService.addDirector(directorMapper.fromDto(directorDto));
        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri()
                .path("/{id}")
                .build(Map.of("id", director.getId()));
        return ResponseEntity.created(uri).body(directorMapper.toDto(director));
    }
}
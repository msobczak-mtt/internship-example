package vod.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@ToString(exclude = "cinemas")
public class Movie {

    private int id;
    private String title;
    private String poster;
    private Director director;
    private List<Cinema> cinemas = new ArrayList<>();

    public Movie(int id, String title, String poster, Director director) {
        this.id = id;
        this.title = title;
        this.poster = poster;
        this.director = director;
    }

    public void addCinema(Cinema c) {
        this.cinemas.add(c);
    }


}

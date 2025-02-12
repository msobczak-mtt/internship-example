package vod.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class Cinema {

    private int id;
    private String name;
    private String logo;
    private List<Movie> movies = new ArrayList<>();

    public Cinema(int id, String name, String logo) {
        this.id = id;
        this.name = name;
        this.logo = logo;
    }

    public void addMovie(Movie m) {
        this.movies.add(m);
    }

}

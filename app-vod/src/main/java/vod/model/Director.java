package vod.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@ToString(exclude = "movies")
public class Director {

    private int id;
    private String firstName;
    private String lastName;
    private List<Movie> movies = new ArrayList<>();

    public Director(int id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public void addMovie(Movie m) {
        this.movies.add(m);
    }

}

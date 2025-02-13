package vod.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
public class Cinema {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String logo;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name="movie_cinema",
            joinColumns = @JoinColumn(name="cinema_id"),
            inverseJoinColumns = @JoinColumn(name="movie_id")
    )
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

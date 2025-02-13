package vod.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@ToString(exclude = "cinemas")
@Entity
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String title;
    private String poster;
    @ManyToOne
    @JoinColumn(name = "director_id")
    private Director director;
    @ManyToMany(mappedBy = "movies")
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

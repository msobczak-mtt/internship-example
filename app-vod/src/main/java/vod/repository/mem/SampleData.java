package vod.repository.mem;

import vod.model.Cinema;
import vod.model.Director;
import vod.model.Movie;

class SampleData {

    static {

        Director smarzowski = new Director(1, "Wojciech", "Smarzowski");
        Director vega = new Director(2, "Patryk", "Vega");
        Director wajda = new Director(3, "Andrzej", "Wajda");
        Director skolimowski = new Director(4, "Jerzy", "Skolimowski");
        Director holland = new Director(5, "Agnieszka", "Holland");

        Movie drogowka = new Movie(1, "Drogowka", "https://fwcdn.pl/fpo/19/79/631979/7707357.6.jpg", smarzowski);
        Movie wesele = new Movie(2, "Wesele", "https://fwcdn.pl/fpo/40/98/124098/7521214.6.jpg", smarzowski);

        Movie polityka = new Movie(3, "Polityka", "https://i.iplsc.com/-/00094J03E94SMPSS-C122.jpg", vega);
        Movie pitbul = new Movie(4, "Pitbul", "https://bi.im-g.pl/im/5b/9b/12/z19510363V,-Pitbull--Nowe-porzadki---rez--Patryk-Vega--plakat.jpg", vega);

        Movie popiolDiament = new Movie(5, "Popiol i diament", "https://upload.wikimedia.org/wikipedia/commons/6/6f/Tomasz_W%C3%B3jcik_-_Ashes_and_Diamonds.jpg", wajda);
        Movie tatarak = new Movie(6, "Tatarak", "http://gapla.fn.org.pl/public/cache/P21829-483x700.jpg", wajda);

        Movie io = new Movie(7, "Io", "https://gutekfilm.pl/uploads/x750/io-plakatpl-oscar-lq_1.jpg", skolimowski);
        Movie ferdydurke = new Movie(8, "Ferdydurke", "http://gapla.fn.org.pl/public/cache/P19423-483x700.jpg", skolimowski);

        Movie europaEuropa = new Movie(9, "Europa, Europa", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR5JdpqeTXFvoTfsljYkJpjFscwQn9cXg6m3HyxAdHNBsYeuuhb", holland);
        Movie pokot = new Movie(10, "Pokot", "https://encrypted-tbn3.gstatic.com/images?q=tbn:ANd9GcTG77NN3PQ-wjr29Onws-_lCnYCUxbfpxSPqWWEE7MJS8qTuhmJ", holland);

        bind(drogowka, smarzowski);
        bind(wesele, smarzowski);

        bind(polityka, vega);
        bind(pitbul, vega);

        bind(popiolDiament, wajda);
        bind(tatarak, wajda);

        bind(io, skolimowski);
        bind(ferdydurke, skolimowski);

        bind(europaEuropa, holland);
        bind(pokot, holland);

        Cinema kinoteka = new Cinema(1, "Kinoteka", "https://www.kinoteka.pl/img/logo.png");
        Cinema podBaranami = new Cinema(2, "Kino pod Baranami", "http://www.festiwalfilmuniemego.pl/wp-content/uploads/2015/11/Kino-pod-Baranami.png");
        Cinema noweHoryzonty = new Cinema(3, "Kino Nowe Horyzonty", "https://i2.wp.com/garretreza.pl/wp-content/uploads/2018/07/nh.jpg");
        Cinema zak = new Cinema(4, "Kino Zak", "https://static2.s-trojmiasto.pl/zdj/c/n/19/2276/250x0/2276445.jpg");

        bind(kinoteka, wesele);
        bind(kinoteka, tatarak);
        bind(europaEuropa, holland);

        bind(noweHoryzonty, wesele);
        bind(noweHoryzonty, drogowka);
        bind(noweHoryzonty, polityka);

        bind(zak, tatarak);
        bind(zak, io);

        bind(podBaranami, io);
        bind(podBaranami, polityka);
        bind(podBaranami, pokot);

        addMovie(drogowka);
        addMovie(wesele);
        addMovie(polityka);
        addMovie(pitbul);
        addMovie(popiolDiament);
        addMovie(tatarak);
        addMovie(io);
        addMovie(ferdydurke);
        addMovie(pokot);
        addMovie(europaEuropa);

        addDirector(smarzowski);
        addDirector(vega);
        addDirector(wajda);
        addDirector(skolimowski);
        addDirector(holland);

        addCinema(kinoteka);
        addCinema(podBaranami);
        addCinema(noweHoryzonty);
        addCinema(zak);
    }

    private static void addMovie(Movie m){
        MemMovieDao.movies.add(m);
    }

    private static void addDirector(Director d){
        MemDirectorDao.directors.add(d);
    }

    private static void addCinema(Cinema c){
        MemCinemaDao.cinemas.add(c);
    }

    private static void bind(Cinema c, Movie m) {
        c.addMovie(m);
        m.addCinema(c);
    }

    private static void bind(Movie m, Director d) {
        d.addMovie(m);
        m.setDirector(d);
    }

}

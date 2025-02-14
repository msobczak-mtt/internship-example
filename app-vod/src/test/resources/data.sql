INSERT INTO
    `cinema`(`id`, `logo`, `name`)
VALUES
    ('1', 'https://upload.wikimedia.org/wikipedia/fr/2/25/Logo-La-Cinémathèque-française_%28since_2016%29.png',
     'Cinematheque Francaise'),
    ('2', 'https://static.wikia.nocookie.net/logopedia/images/4/43/British_Film_Institute.svg/revision/latest?cb=20210129162803', 'BFI Southbank'),
    ('3', 'https://images.ctfassets.net/4wrp2um278k7/41ayyM1836M4YsmUUOw0u8/250c79152ca84a62b8ee3278032f5b50/Eye_Primary_Lockup_Black.jpg', 'EYE Filmmuseum'),
    ('4', 'https://upload.wikimedia.org/wikipedia/commons/7/7d/Logo_Kino-Babylon_Berlin.jpg', 'Kino Babylon');

INSERT INTO
    `director`(`id`, `firstname`, `lastname`)
VALUES
    ('1', 'Werner', 'Herzog'),
    ('2', 'Lars', 'von Trier'),
    ('3', 'Jean-Luc', 'Godard'),
    ('4', 'Ingmar', 'Bergman');

INSERT INTO
    `movie`(`rate`, `id`, `title`, `director_id`, `poster`)
VALUES
    (0, 1, 'Fitzcarraldo', 1, 'https://resizing.flixster.com/-XZAfHZM39UwaGJIFWKAE8fS0ak=/v3/t/assets/p7140_p_v8_ar.jpg'),
    (0, 2, 'Aguirre, the Wrath of God', 1, 'https://images.justwatch.com/poster/223371515/s166/aguirre-gniew-bozy.webp'),
    (0, 3, 'Dancer in the Dark', 2, 'https://m.media-amazon.com/images/I/91wOysDKjgL._AC_SY879_.jpg'),
    (0, 4, 'Melancholia', 2, 'https://m.media-amazon.com/images/I/91-Mpz+m91L._SY300_.jpg'),
    (0, 5, 'Vivre Sa Vie', 3, 'https://upload.wikimedia.org/wikipedia/en/9/97/VivresaViePoster.jpg?20060205200747'),
    (0, 6, 'Bande a Part', 3, 'https://i.ebayimg.com/images/g/LaEAAOSwyHBk6RYN/s-l1600.webp'),
    (0, 7, 'The Seventh Seal', 4, 'https://images.savoysystems.co.uk/GCL/500539.jpg');

INSERT INTO
    `movie_cinema`(`movie_id`, `cinema_id`)
VALUES
    ('1', '1'),
    ('2', '1'),
    ('2', '4'),
    ('3', '2'),
    ('7', '1'),
    ('6', '3'),
    ('5', '2'),
    ('1', '3');

ALTER TABLE movie ALTER COLUMN id RESTART WITH 100;
CREATE TABLE `cinema`
(
    `id`   int          NOT NULL AUTO_INCREMENT,
    `logo` varchar(255) NOT NULL,
    `name` varchar(255) NOT NULL,
    PRIMARY KEY (`id`)
);

CREATE TABLE `director`
(
    `id`        int          NOT NULL AUTO_INCREMENT,
    `firstname` varchar(255) NOT NULL,
    `lastname`  varchar(255) NOT NULL,
    PRIMARY KEY (`id`)
);

CREATE TABLE `movie`
(
    `id`          int          NOT NULL AUTO_INCREMENT,
    `poster`      varchar(255) NOT NULL,
    `title`       varchar(255) NOT NULL,
    `director_id` int   DEFAULT NULL,
    PRIMARY KEY (`id`)
);

CREATE TABLE `rating`
(
    `id`          int          NOT NULL AUTO_INCREMENT,
    `rate`      float DEFAULT NULL,
    `movie_id` int   DEFAULT NULL,
    PRIMARY KEY (`id`)
);

CREATE TABLE `movie_cinema`
(
    `movie_id`  int DEFAULT NULL,
    `cinema_id` int DEFAULT NULL
);

INSERT INTO `cinema`(`id`, `logo`, `name`)
VALUES ('1', 'https://upload.wikimedia.org/wikipedia/commons/thumb/9/9a/Multikino_logo.png/1198px-Multikino_logo.png',
        'Multikino');
INSERT INTO `cinema`(`id`, `logo`, `name`)
VALUES ('2', 'https://upload.wikimedia.org/wikipedia/commons/thumb/6/61/Imax.svg/330px-Imax.svg.png', 'IMAX');
INSERT INTO `cinema`(`id`, `logo`, `name`)
VALUES ('3', 'https://www.cinema-city.pl/xmedia/img/10103/logo.svg', 'Cinema City');

INSERT INTO `director`(`id`, `firstname`, `lastname`)
VALUES ('1', 'Steven', 'Spielberg');
INSERT INTO `director`(`id`, `firstname`, `lastname`)
VALUES ('2', 'Woody', 'Allen');
INSERT INTO `director`(`id`, `firstname`, `lastname`)
VALUES ('3', 'Guy', 'Ritchie');

INSERT INTO `movie`(`id`, `poster`, `title`, `director_id`)
VALUES ('1', 'https://static.posters.cz/image/750webp/73584.webp', 'Jaws', '1');
INSERT INTO `movie`(`id`, `poster`, `title`, `director_id`)
VALUES ('2', 'https://fwcdn.pl/fpo/01/79/179/7710998.6.jpg', 'Saving Private Ryan', '1');
INSERT INTO `movie`(`id`, `poster`, `title`, `director_id`)
VALUES ('3', 'https://fwcdn.pl/fpo/12/15/1215/6918508.6.jpg', 'E.T.', '1');
INSERT INTO `movie`(`id`, `poster`, `title`, `director_id`)
VALUES ('4', 'https://upload.wikimedia.org/wikipedia/en/0/05/Vicky_Cristina_Barcelona_film_poster.png',
        'Vicky Cristina Barcelona', '2');
INSERT INTO `movie`(`id`, `poster`, `title`, `director_id`)
VALUES ('5', 'https://upload.wikimedia.org/wikipedia/en/thumb/f/f3/Manhattan-poster01.jpg/220px-Manhattan-poster01.jpg',
        'Manhattan', '2');
INSERT INTO `movie`(`id`, `poster`, `title`, `director_id`)
VALUES ('6', 'https://fwcdn.pl/fpo/13/26/1326/7635628.6.jpg', 'Snatch', '3');
INSERT INTO `movie`(`id`, `poster`, `title`, `director_id`)
VALUES ('7', 'https://fwcdn.pl/fpo/19/97/441997/7239460.6.jpg', 'RockNRolla', '3');

INSERT INTO `rating`(`id`, `rate`, `movie_id`)
VALUES ('1', '1.1', '1');
INSERT INTO `rating`(`id`, `rate`, `movie_id`)
VALUES ('2', '2.2', '2');
INSERT INTO `rating`(`id`, `rate`, `movie_id`)
VALUES ('3', '3.3', '3');
INSERT INTO `rating`(`id`, `rate`, `movie_id`)
VALUES ('4', '4.4', '4');
INSERT INTO `rating`(`id`, `rate`, `movie_id`)
VALUES ('5', '4.9', '5');
INSERT INTO `rating`(`id`, `rate`, `movie_id`)
VALUES ('6', '4.3', '5');
INSERT INTO `rating`(`id`, `rate`, `movie_id`)
VALUES ('7', '2.1', '5');
INSERT INTO `rating`(`id`, `rate`, `movie_id`)
VALUES ('8', '1.1', '4');

INSERT INTO `movie_cinema`(`movie_id`, `cinema_id`)
VALUES ('1', '1');
INSERT INTO `movie_cinema`(`movie_id`, `cinema_id`)
VALUES ('1', '3');
INSERT INTO `movie_cinema`(`movie_id`, `cinema_id`)
VALUES ('2', '3');
INSERT INTO `movie_cinema`(`movie_id`, `cinema_id`)
VALUES ('3', '1');
INSERT INTO `movie_cinema`(`movie_id`, `cinema_id`)
VALUES ('3', '2');
INSERT INTO `movie_cinema`(`movie_id`, `cinema_id`)
VALUES ('4', '1');
INSERT INTO `movie_cinema`(`movie_id`, `cinema_id`)
VALUES ('4', '3');
INSERT INTO `movie_cinema`(`movie_id`, `cinema_id`)
VALUES ('5', '2');
INSERT INTO `movie_cinema`(`movie_id`, `cinema_id`)
VALUES ('5', '3');
INSERT INTO `movie_cinema`(`movie_id`, `cinema_id`)
VALUES ('6', '1');
INSERT INTO `movie_cinema`(`movie_id`, `cinema_id`)
VALUES ('7', '2');
















CREATE TABLE user
(
    id       int primary key auto_increment,
    username VARCHAR(255),
    password VARCHAR(255)
);

CREATE TABLE role
(
    id       int primary key auto_increment,
    username VARCHAR(255),
    role     VARCHAR(255)
);

INSERT INTO user(username, password)
VALUES ('dbuser1', 'dbuser1'),
       ('dbuser2', 'dbuser2'),
       ('dbuser3', 'dbuser3');


INSERT INTO role(username, role)
VALUES ('dbuser1', 'ROLE_ADMIN'),
       ('dbuser2', 'ROLE_AUTHOR_ADMIN'),
       ('dbuser3', 'ROLE_BOOK_ADMIN');





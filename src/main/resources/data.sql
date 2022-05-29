MERGE INTO "RatingMPAA" KEY ("RatingID") VALUES (1, '6+', 'Детский');
MERGE INTO "RatingMPAA" KEY ("RatingID") VALUES (2, '12+', 'Детский++');
MERGE INTO "RatingMPAA" KEY ("RatingID") VALUES (3, '16+', 'Почти взрослый');
MERGE INTO "RatingMPAA" KEY ("RatingID") VALUES (4, '18+', 'Взрослый');

MERGE INTO "Genre" KEY ("GenreID") VALUES (1, 'Боевик');
MERGE INTO "Genre" KEY ("GenreID") VALUES (2, 'Фантастика');
MERGE INTO "Genre" KEY ("GenreID") VALUES (3, 'Комедия');
MERGE INTO "Genre" KEY ("GenreID") VALUES (4, 'Триллер');
MERGE INTO "Genre" KEY ("GenreID") VALUES (5, 'Драма');
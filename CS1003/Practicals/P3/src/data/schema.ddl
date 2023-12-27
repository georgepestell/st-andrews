CREATE TABLE 'Movie' (
	'movie_id' INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
	'title' VARCHAR(50) NOT NULL,
	'releaseDate' INTEGER,
	'runtime' INTEGER,
	'genre' VARCHAR(50),
	'plot' VARCHAR(200)
);

CREATE TABLE Review (
	'rating_id' INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
	'movie_id' INTEGER NOT NULL,
	'rating' INTEGER NOT NULL,
	'notes' VARCHAR(150),
	FOREIGN KEY ('movie_id') REFERENCES Movie('movie_id')
);

CREATE TABLE Person(
	'person_id' INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
	'name' VARCHAR(50) NOT NULL,
	'dob' DATE
);

CREATE TABLE MoviePerson(
	'movieperson_id' INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
	'movie_id' INTEGER NOT NULL,
	'person_id' INTEGER NOT NULL,
	'role' VARCHAR(50) NOT NULL,
	'character_name' VARCHAR(50),
	FOREIGN KEY ('movie_id') REFERENCES Movie('movie_id'),
	FOREIGN KEY ('person_id') REFERENCES Person('person_id')
);

CREATE TABLE Award(
	'award_id' INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
	'name' VARCHAR(50) NOT NULL,
	'event' VARCHAR(50) NOT NULL
);

CREATE TABLE MovieAward(
	'award_id' INTEGER NOT NULL,
	'movie_id' INTEGER NOT NULL,
	'year' INTEGER NOT NULL,
	FOREIGN KEY ('award_id') REFERENCES Award('award_id'),
	FOREIGN KEY ('movie_id') REFERENCES Movie('movie_id'),
	PRIMARY KEY (award_id, year)
);

CREATE TABLE MoviePersonAward(
	'award_id' INTEGER NOT NULL,
	'movieperson_id' INTEGER NOT NULL,
	'year' INTEGER NOT NULL,
	FOREIGN KEY ('award_id') REFERENCES Award('award_id'),
	FOREIGN KEY ('movieperson_id') REFERENCES MoviePerson('movieperson_id')
);

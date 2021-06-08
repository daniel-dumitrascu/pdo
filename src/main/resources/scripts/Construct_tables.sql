-- In this script we are going to create the tables for our database
-- The tables are going to be created in this order:

-- STORAGE table
-- BACKUP table
-- PEOPLE table
-- USERS table
-- PICTURE_ALBUMS table
-- APPS table
-- APP_TAGS table
-- GAMES table
-- GAME_TYPE_TAGS table
-- SONGS table
-- MUSIC_PLAYLISTS table
-- MUSIC_PLAYLIST_ENTRY table
-- BOOKS table
-- AUTHORS table
-- VIDEO table
-- EXTRA_VIDEO_INFO_DOC table
-- EXTRA_VIDEO_INFO_SERIES table
-- POSTER table
-- VIDEO_GENRES table
-- VIDEO_COUNTRY table
-- VIDEO_LANGUAGE table
-- VIDEO_TAGS table
-- ACTORS table
-- VIDEO_PLAYLISTS table
-- VIDEO_PLAYLIST_ENTRY table

-- Depending in which schema you run this script you must set ms_main or ms_test
SET search_path TO "MS_MAIN";

--------------------------------------------------------------------------------------------------------------------------
------------------------------------------------- Storage table ----------------------------------------------------------
--------------------------------------------------------------------------------------------------------------------------

-- TypeOfStorage can have the folowing values:
-- CD 1
-- DVD 2
-- Blue-Ray 3
-- HDD 4
-- SSD 5
-- Steam 6
-- USB stick 7

CREATE TABLE STORAGE
(
        Storage_Key       serial           PRIMARY KEY,
        Storage_Name      TEXT             NOT NULL,
        Type_Of_Storage   SMALLINT         NOT NULL,
        Storage_In_Use    BOOLEAN          NOT NULL,
        In_Use_Starting   date             NOT NULL,
        Has_Errors        BOOLEAN          DEFAULT FALSE,
        Observations      TEXT,

        CONSTRAINT storage_unique_name UNIQUE(Storage_Name)
);

--------------------------------------------------------------------------------------------------------------------------
------------------------------------------------- Backup table -----------------------------------------------------------
--------------------------------------------------------------------------------------------------------------------------

CREATE TABLE BACKUP
(
        Backup_Key       serial PRIMARY KEY,
        Main_Storage     serial,
        Backup_Storage   serial,

        FOREIGN KEY (Main_Storage)   REFERENCES STORAGE(Storage_Key),
        FOREIGN KEY (Backup_Storage) REFERENCES STORAGE(Storage_Key),

        CONSTRAINT backup_storage_unique UNIQUE(Main_Storage, Backup_Storage),
        CONSTRAINT backup_non_self_ref CHECK (Main_Storage != Backup_Storage)
);

--------------------------------------------------------------------------------------------------------------------------
------------------------------------------------- People table -----------------------------------------------------------
--------------------------------------------------------------------------------------------------------------------------

-- The Role can have one of this values
-- Actor 1
-- Director 2
-- Writer 3
-- Singer 4

CREATE TABLE PEOPLE
(
        People_Key              serial          PRIMARY KEY,
        NAME                    TEXT            NOT NULL,
        Email                   TEXT,
        Nationality             TEXT,
        ROLE                    SMALLINT        NOT NULL,
        Tmdb_Id                 INT,
        Gender                  SMALLINT,
        Birthday                date,
        BirthPlace              TEXT,
        Deathday                date,
        Biography               TEXT,
        Tmdb_Poster_Path        TEXT,
        Extern_Fetching_Status  SMALLINT,

        CONSTRAINT people_unique_name UNIQUE(NAME)
);

--------------------------------------------------------------------------------------------------------------------------
------------------------------------------------- Users table ------------------------------------------------------------
--------------------------------------------------------------------------------------------------------------------------

CREATE TABLE USERS
(
        User_Key      	serial          PRIMARY KEY,
        People_key      serial,
        Username     	TEXT			NOT NULL,
        Password        TEXT        	NOT NULL,
		Email			TEXT,

		FOREIGN KEY (People_key)   REFERENCES PEOPLE(People_key),
        CONSTRAINT users_unique_username UNIQUE(Username)
);

--------------------------------------------------------------------------------------------------------------------------
------------------------------------------------- Picture_albums table ---------------------------------------------------
--------------------------------------------------------------------------------------------------------------------------

-- Type can have one of this values:
-- Personal 1
-- History 2
-- Art 3
-- Nature 4

CREATE TABLE PICTURE_ALBUMS
(
        Album_Key           serial          PRIMARY KEY,
        Title               TEXT            NOT NULL,
        Nr_Of_Pictures      SMALLINT        NOT NULL,
        TYPE                SMALLINT        NOT NULL,
        Shot_Date           date,
        Added_To_Db_Date    date            NOT NULL,
        Storage_Name        TEXT,

		CONSTRAINT picture_albums_unique_title UNIQUE(Title),
        CONSTRAINT picture_albums_nr CHECK(Nr_Of_Pictures > 0)
);

--------------------------------------------------------------------------------------------------------------------------
------------------------------------------------- Apps table -------------------------------------------------------------
--------------------------------------------------------------------------------------------------------------------------

-- Type can have one of this values:
-- Game 1
-- Archive 2
-- Network 3
-- Office 4

-- Platform can have one of this values:
-- Windows 1
-- Linux 2
-- Mac 3
-- PS3 4
-- PS4 5
-- iPhone 6
-- Android 7

-- Licence_Type can have one of this values:
-- Free 1
-- Free non-commercial 2
-- Commercial 3

CREATE TABLE APPS
(
        App_Key             serial          PRIMARY KEY,
        Title               TEXT            NOT NULL,
        TYPE                SMALLINT        NOT NULL,
        Description         TEXT            DEFAULT NULL,
        Playform            SMALLINT        NOT NULL,
        Licence_Type        SMALLINT,
        Released_Date       date,
        Added_To_Db_Date    date            NOT NULL,
        Storage_Name        TEXT,

        CONSTRAINT apps_unique_title UNIQUE(Title)
);

--------------------------------------------------------------------------------------------------------------------------
------------------------------------------------- App_Tags table -------------------------------------------------------------
--------------------------------------------------------------------------------------------------------------------------
CREATE TABLE APP_TAGS
(
        App_Tag_Key         serial          PRIMARY KEY,
        App_Key             serial,
        Tag_Name            TEXT,

        FOREIGN KEY (App_Key) REFERENCES APPS(App_Key),
        CONSTRAINT app_tags_unique_pair UNIQUE(App_Key, Tag_Name)
);

--------------------------------------------------------------------------------------------------------------------------
------------------------------------------------- Games table ------------------------------------------------------------
--------------------------------------------------------------------------------------------------------------------------

-- Personal_Score should be between 1 and 10

CREATE TABLE GAMES
(
        Game_Key             serial          PRIMARY KEY,
        App_Key              serial,
        Play_It              BOOLEAN         DEFAULT FALSE,
        Personal_Score       decimal,
        Developer            TEXT,
        Publisher            TEXT,
        Youtube_Link         TEXT,

        FOREIGN KEY (App_Key) REFERENCES APPS(App_Key),
		CONSTRAINT games_score_range CHECK(Personal_Score::NUMERIC >= 1.0 AND Personal_Score::NUMERIC <= 10.0)
);

--------------------------------------------------------------------------------------------------------------------------
------------------------------------------------- Game_Type_Tags table ---------------------------------------------------
--------------------------------------------------------------------------------------------------------------------------

-- Type can have one of this values:
-- Strategy 1
-- Puzzle 2
-- RPG 3
-- Shooter 4
-- Platformer 5
-- Racing 6
-- Adventure 7

CREATE TABLE GAME_TYPE_TAGS
(
        Game_Type_Tag_Key       serial       PRIMARY KEY,
        Game_Key                serial,
        TYPE                    SMALLINT     NOT NULL,

        FOREIGN KEY (Game_Key) REFERENCES GAMES(Game_Key),
		CONSTRAINT game_type_tags_unique UNIQUE(Game_Key, TYPE)
);

--------------------------------------------------------------------------------------------------------------------------
------------------------------------------------- Songs table ------------------------------------------------------------
--------------------------------------------------------------------------------------------------------------------------

-- File_Type can have one of this values:
-- mp3 1
-- wave 2

-- Quality can have one of this values:
-- Excelent 1
-- Good 2
-- Bad 3

-- Type can have one of this values:
-- Ambiental 1
-- House 2
-- Rock 3

CREATE TABLE SONGS
(
        Song_Key              serial            PRIMARY KEY,
        Artist_Key            serial,
        Title                 TEXT              NOT NULL,
        LANGUAGE              TEXT,
        File_Type             SMALLINT,
        Quality               SMALLINT,
        Album_Name            TEXT,
        TYPE                  SMALLINT          NOT NULL,
        Released_Date         date,
        Added_To_Db_Date      date              NOT NULL,
        Storage_Name          TEXT,

        FOREIGN KEY (Artist_Key) REFERENCES PEOPLE(People_Key),
        CONSTRAINT song_unique_title UNIQUE(Title)
);

--------------------------------------------------------------------------------------------------------------------------
------------------------------------------------- Music_playlists table --------------------------------------------------
--------------------------------------------------------------------------------------------------------------------------

CREATE TABLE MUSIC_PLAYLISTS
(
        Playlist_Key            serial          PRIMARY KEY,
        Title                   TEXT            NOT NULL,
        Description             TEXT,
        Created                 date            NOT NULL,

        CONSTRAINT music_playlist_unique_title UNIQUE(Title)
);

--------------------------------------------------------------------------------------------------------------------------
------------------------------------------------- Music_playlist_entry table ---------------------------------------------
--------------------------------------------------------------------------------------------------------------------------

CREATE TABLE MUSIC_PLAYLIST_ENTRY
(
        Playlist_Entry_Key      serial          PRIMARY KEY,
        Song_Key                serial,
        Playlist_Key            serial,
        Created                 date            NOT NULL,

        FOREIGN KEY (Song_Key) REFERENCES SONGS(Song_Key),
        FOREIGN KEY (Playlist_Key) REFERENCES MUSIC_PLAYLISTS(Playlist_Key)
);

--------------------------------------------------------------------------------------------------------------------------
------------------------------------------------- Books table ------------------------------------------------------------
--------------------------------------------------------------------------------------------------------------------------

-- File_Type can have one of this values:
-- Hibliography 1
-- Drama 2
-- Comedy 3
-- Mystery 4
-- Horror 5
-- SF 7
-- Fantasy 8
-- History 9

CREATE TABLE BOOKS
(
        Book_Key                serial          PRIMARY KEY,
        Title                   TEXT            NOT NULL,
        ISBN10                  VARCHAR(10),
        ISBN13                  VARCHAR(13),
        LANGUAGE                TEXT,
        Read_It                 BOOLEAN         DEFAULT FALSE,
        TYPE                    SMALLINT        NOT NULL,
        Personal_Score          decimal,
        Released_Date           date,
        Added_To_Db_Date        date            NOT NULL,
        Storage_Name            TEXT,

        CONSTRAINT books_title_unique UNIQUE(Title),
		CONSTRAINT books_score_range CHECK(Personal_Score::NUMERIC >= 1.0 AND Personal_Score::NUMERIC <= 10.0)
);

--------------------------------------------------------------------------------------------------------------------------
------------------------------------------------- Authors table ----------------------------------------------------------
--------------------------------------------------------------------------------------------------------------------------

CREATE TABLE AUTHORS
(
        Authors_Key 	serial 		PRIMARY KEY,
        People_Key 		serial,
        Book_Key 		serial,

        FOREIGN KEY (People_Key) REFERENCES PEOPLE(People_Key),
        FOREIGN KEY (Book_Key) REFERENCES BOOKS(Book_Key),
        CONSTRAINT authors_unique_entry UNIQUE(People_Key, Book_Key)
);

--------------------------------------------------------------------------------------------------------------------------
------------------------------------------------- Video table ------------------------------------------------------------
--------------------------------------------------------------------------------------------------------------------------

CREATE TABLE VIDEO
(
        Video_Key               serial          PRIMARY KEY,
        Title                   TEXT            NOT NULL,
        Also_Known_As           TEXT,
        Description             TEXT,
        Video_Type              TEXT            NOT NULL,
        Video_Status            TEXT,
        Budget                  INT,
        Revenue                 INT,
        Saw_It                  BOOLEAN         DEFAULT FALSE,
        Quality                 TEXT,
        Personal_Score          decimal,
        Release_Year            TEXT,
        Release_Date            date,
        Has_Romanian_Sub        BOOLEAN,
        Has_English_Sub         BOOLEAN,
        Runtime                 SMALLINT,
        Added_To_Db_Date        date            NOT NULL,
        Extern_Fetching_Status  SMALLINT,
        Internet_Score          decimal,
        Internet_Vote_Count     INT,
        Imdb_Id                 TEXT,
        Imdb_Link               TEXT,
        Tmdb_Id                 TEXT,
        Tmdb_Poster_Path        TEXT,
        Youtube_Link            TEXT,
        Storage_Name            TEXT,

        CONSTRAINT video_personal_score_range CHECK(Personal_Score::NUMERIC >= 0.0 AND Personal_Score::NUMERIC <= 10.0),
        CONSTRAINT video_imdb_score_range CHECK(Internet_Score::NUMERIC >= 0.0 AND Internet_Score::NUMERIC <= 10.0)
);

--------------------------------------------------------------------------------------------------------------------------
------------------------------------------------- EXTRA_VIDEO_INFO_DOC table ---------------------------------------------
--------------------------------------------------------------------------------------------------------------------------

CREATE TABLE EXTRA_VIDEO_INFO_DOC
(
        Extra_Video_Info_Doc_Key         serial          PRIMARY KEY,
        Video_Key                        serial,
        Is_Series                        BOOLEAN,
        Docs_In_Series                   SMALLINT,

        FOREIGN KEY (Video_Key) REFERENCES VIDEO(Video_Key),
        CONSTRAINT documentaries_unique_video_key UNIQUE(Video_Key),
        CONSTRAINT documentaries_check_series CHECK((Is_Series IS NULL AND Docs_In_Series IS NULL) OR
                                                    (Is_Series IS NOT NULL AND Is_Series = FALSE AND Docs_In_Series IS NULL) OR
                                                    (Is_Series IS NOT NULL AND Is_Series = TRUE AND (Docs_In_Series IS NULL OR Docs_In_Series IS NOT NULL)))
);

--------------------------------------------------------------------------------------------------------------------------
------------------------------------------------- EXTRA_VIDEO_INFO_SERIES table ------------------------------------------
--------------------------------------------------------------------------------------------------------------------------

CREATE TABLE EXTRA_VIDEO_INFO_SERIES
(
        Extra_Video_Info_Series_Key              serial          PRIMARY KEY,
        Video_Key                                serial,
        Episode_Runtime                          SMALLINT,
        Nr_Of_Seasons                            SMALLINT,
        In_Development                           BOOLEAN,

        FOREIGN KEY (Video_Key) REFERENCES VIDEO(Video_Key)
);

--------------------------------------------------------------------------------------------------------------------------
------------------------------------------------- Video_genres table -----------------------------------------------------
--------------------------------------------------------------------------------------------------------------------------

CREATE TABLE VIDEO_GENRES
(
        Video_Genre_Key         serial          PRIMARY KEY,
        Video_Key               serial,
        Genre                   TEXT,

        FOREIGN KEY (Video_Key) REFERENCES VIDEO(Video_Key),
        CONSTRAINT video_genres_unique_pair UNIQUE(Video_Key, Genre)
);

--------------------------------------------------------------------------------------------------------------------------
------------------------------------------------- Video_Country table ----------------------------------------------------
--------------------------------------------------------------------------------------------------------------------------
CREATE TABLE VIDEO_COUNTRY
(
        Video_Country_Key         serial          PRIMARY KEY,
        Video_Key                 serial,
        Country                   TEXT,

        FOREIGN KEY (Video_Key) REFERENCES VIDEO(Video_Key),
        CONSTRAINT video_country_unique_pair UNIQUE(Video_Key, Country)
);

--------------------------------------------------------------------------------------------------------------------------
------------------------------------------------- Video_Language table ---------------------------------------------------
--------------------------------------------------------------------------------------------------------------------------
CREATE TABLE VIDEO_LANGUAGE
(
        Video_Language_Key         serial          PRIMARY KEY,
        Video_Key                  serial,
        LANGUAGE                   TEXT,

        FOREIGN KEY (Video_Key) REFERENCES VIDEO(Video_Key),
        CONSTRAINT video_language_unique_pair UNIQUE(Video_Key, LANGUAGE)
);

--------------------------------------------------------------------------------------------------------------------------
------------------------------------------------- Video_Tags table ---------------------------------------------------
--------------------------------------------------------------------------------------------------------------------------
CREATE TABLE VIDEO_TAGS
(
        Video_Tag_Key         serial          PRIMARY KEY,
        Video_Key             serial,
        Tag_Name              TEXT,

        FOREIGN KEY (Video_Key) REFERENCES VIDEO(Video_Key),
        CONSTRAINT video_tags_unique_pair UNIQUE(Video_Key, Tag_Name)
);

--------------------------------------------------------------------------------------------------------------------------
------------------------------------------------- Actors table -----------------------------------------------------------
--------------------------------------------------------------------------------------------------------------------------
CREATE TABLE ACTOR_JOBS
(
        Actor_Key           serial     PRIMARY KEY,
        People_Key          serial,
        Video_Key           serial,

        FOREIGN KEY (People_Key) REFERENCES PEOPLE(People_Key),
        FOREIGN KEY (Video_Key) REFERENCES VIDEO(Video_Key),

        CONSTRAINT actors_unique_pair UNIQUE(People_Key, Video_Key)
);

--------------------------------------------------------------------------------------------------------------------------
------------------------------------------------- Movie_playlists table --------------------------------------------------
--------------------------------------------------------------------------------------------------------------------------

CREATE TABLE VIDEO_PLAYLISTS
(
        Playlist_Key            serial          PRIMARY KEY,
        Title                   TEXT            NOT NULL,
        Description             TEXT,
        Created                 date            NOT NULL,

        CONSTRAINT video_playlists_unique UNIQUE(Playlist_Key, Title)
);

--------------------------------------------------------------------------------------------------------------------------
------------------------------------------------- Movie_playlist_entry table --------------------------------------------------
--------------------------------------------------------------------------------------------------------------------------

CREATE TABLE VIDEO_PLAYLIST_ENTRY
(
        Playlist_Entry_Key       serial          PRIMARY KEY,
        Video_Key                serial,
        Playlist_Key             serial,
        Created                  date            NOT NULL,

        FOREIGN KEY (Video_Key) REFERENCES VIDEO(Video_Key),
        FOREIGN KEY (Playlist_Key) REFERENCES VIDEO_PLAYLISTS(Playlist_Key)
);
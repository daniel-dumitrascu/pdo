-- Functions present in this file:

-- GenerateNextVideoKey
-- AddVideo
-- AddExtraVideoInfoDoc
-- AddExtraVideoInfoSeries
-- AddVideoGenre
-- AddVideoLanguage
-- AddVideoCountry
-- GetVideoKeyByName
-- AddActor

------------------------------------------------------------------
-- Function used to generate a new primary key for the VIDEO table
------------------------------------------------------------------
CREATE OR REPLACE FUNCTION mspublic.GenerateNextVideoKey()
    RETURNS INTEGER LANGUAGE plpgsql
AS $function$
    BEGIN
        return nextval('mspublic.video_video_key_seq'::regclass);
    END;
$function$;

---------------------------------------------------------------------------
-- Function used to insert a new video entity into the VIDEO table
-- We should not call this directly but the derived functions from this one
---------------------------------------------------------------------------
CREATE OR REPLACE FUNCTION mspublic.AddVideo(inTitle TEXT, inAlsoKnownAs TEXT, inDescription TEXT, inVideoType INT, inSawIt BOOLEAN, 
                                             inQuality INT, inPersonalScore REAL, inReleaseDate TEXT, inHasRomanianSub BOOLEAN, 
                                             inHasEnglishSub BOOLEAN, inRuntime INT, inInternetScore REAL, inOmdbLink TEXT,
                                             inImdbLink TEXT, inYoutubeLink TEXT, inStorage INT)
RETURNS INTEGER LANGUAGE plpgsql
AS $function$
    DECLARE
        video_key INTEGER;
    BEGIN
        SELECT mspublic.GenerateNextVideoKey() INTO video_key;
        INSERT INTO MSPUBLIC.VIDEO (Video_key, Title, Also_Known_As, Description, Video_Type,
                                    Saw_It, Quality, Personal_Score, Release_Date, 
                                    Has_Romanian_Sub, Has_English_Sub, Runtime, Added_To_Db_Date, 
                                    Imdb_Score, Omdb_Link, Imdb_Link, Youtube_Link, Storage_Key) VALUES
        (
                video_key,
                inTitle,
                inAlsoKnownAs,
                inDescription,
                inVideoType,
                inSawIt,
                inQuality,
                inPersonalScore,
                to_date(inReleaseDate, 'DD Mon YYYY'),
                inHasRomanianSub,
                inHasEnglishSub,
                inRuntime,
                now()::date,
                inInternetScore,
                inOmdbLink,
                inImdbLink,
                inYoutubeLink,
                inStorage
        );
        
        return video_key;
    END;
$function$;

---------------------------------------------------------------------------
-- Function used to insert the extra info referring to the Documentary file
-- To call this function we should already have a VIDEO key generated
---------------------------------------------------------------------------
CREATE OR REPLACE FUNCTION mspublic.AddExtraVideoInfoDoc(inVideoKey INT, inIsSeries BOOLEAN, inDocsInSeries INT)
RETURNS INTEGER LANGUAGE plpgsql
AS $function$
    DECLARE
        extraVideoInfoDocKey INT;
    BEGIN
    
    -- if we already have the inVideoKey inserted into MSPUBLIC.EXTRA_VIDEO_INFO_DOC then we just update the information
    -- otherwise, we just insert a new row (as seen below)
    
        SELECT nextval('mspublic.extra_video_info_doc_extra_video_info_doc_key_seq'::regclass) INTO extraVideoInfoDocKey;
        INSERT INTO MSPUBLIC.EXTRA_VIDEO_INFO_DOC (Extra_Video_Info_Doc_Key, Video_Key, Is_Series, Docs_In_Series) VALUES
        (
                extraVideoInfoDocKey,
                inVideoKey,
                inIsSeries,
                inDocsInSeries
        );
        
        return extraVideoInfoDocKey;
    END;
$function$;

----------------------------------------------------------------------
-- Function used to insert the extra info referring to the Series file 
----------------------------------------------------------------------
CREATE OR REPLACE FUNCTION mspublic.AddExtraVideoInfoSeries(inVideoKey INT, inEpisodeRuntime INT, inNrOfSeasons INT, inInDevelopment BOOLEAN)
RETURNS INTEGER LANGUAGE plpgsql
AS $function$
    DECLARE
        extraVideoInfoSeriesKey INT;
    BEGIN
    
    -- if we already have the inVideoKey inserted into MSPUBLIC.EXTRA_VIDEO_INFO_SERIES then we just update the information
    -- otherwise, we just insert a new row (as seen below)
    
        SELECT nextval('mspublic.extra_video_info_series_extra_video_info_series_key_seq'::regclass) INTO extraVideoInfoSeriesKey;
        INSERT INTO MSPUBLIC.EXTRA_VIDEO_INFO_SERIES (Extra_Video_Info_Series_Key, Video_Key, Episode_Runtime, Nr_Of_Seasons, In_Development) VALUES
        (
                extraVideoInfoSeriesKey,
                inVideoKey,
                inEpisodeRuntime,
                inNrOfSeasons,
                inInDevelopment
        );
        
        return extraVideoInfoSeriesKey;
    END;
$function$;

--------------------------------------------------------
-- Function used to insert the genre for a certain video
--------------------------------------------------------
CREATE OR REPLACE FUNCTION mspublic.AddVideoGenre(inVideoName TEXT, inGenre INT)
RETURNS INT LANGUAGE plpgsql
AS $function$
    DECLARE
        vVideoGenre_Key INTEGER;
        vVideo_Key INTEGER;
    BEGIN
        SELECT mspublic.GetVideoKeyByName(inVideoName) INTO vVideo_Key;
        IF vVideo_Key IS NOT NULL THEN
            SELECT nextval('mspublic.video_genres_video_genre_key_seq'::regclass) INTO vVideoGenre_Key;
            INSERT INTO VIDEO_GENRES(Video_Genre_Key, Video_Key, Genre) 
            VALUES
            (
                vVideoGenre_Key,
                vVideo_Key,
                inGenre
            );
            return vVideoGenre_Key;
        ELSE
            return NULL;
        END IF;
    END;
$function$;

----------------------------------------------------
-- Function used to insert languages used in a video
----------------------------------------------------
CREATE OR REPLACE FUNCTION mspublic.AddVideoLanguage(inVideoName TEXT, inLanguage TEXT)
RETURNS INT LANGUAGE plpgsql
AS $function$
    DECLARE
        vVideoLanguage_Key INTEGER;
        vVideo_Key INTEGER;
    BEGIN
        SELECT mspublic.GetVideoKeyByName(inVideoName) INTO vVideo_Key;
        IF vVideo_Key IS NOT NULL THEN
            SELECT nextval('mspublic.video_language_video_language_key_seq'::regclass) INTO vVideoLanguage_Key;
            INSERT INTO VIDEO_LANGUAGE(Video_Language_Key, Video_Key, LANGUAGE) 
            VALUES
            (
                vVideoLanguage_Key,
                vVideo_Key,
                inLanguage
            );
            return vVideoLanguage_Key;
        ELSE
            return NULL;
        END IF;
    END;
$function$;

----------------------------------------------------
-- Function used to insert languages used in a video
----------------------------------------------------
CREATE OR REPLACE FUNCTION mspublic.AddVideoCountry(inVideoName TEXT, inCountry TEXT)
RETURNS INT LANGUAGE plpgsql
AS $function$
    DECLARE
        vVideoCountry_Key INTEGER;
        vVideo_Key INTEGER;
    BEGIN
        SELECT mspublic.GetVideoKeyByName(inVideoName) INTO vVideo_Key;
        IF vVideo_Key IS NOT NULL THEN
            SELECT nextval('mspublic.video_country_video_country_key_seq'::regclass) INTO vVideoCountry_Key;
            INSERT INTO VIDEO_COUNTRY(Video_Country_Key, Video_Key, Country) 
            VALUES
            (
                vVideoCountry_Key,
                vVideo_Key,
                inCountry
            );
            return vVideoCountry_Key;
        ELSE
            return NULL;
        END IF;
    END;
$function$;

-----------------------------------------------------------------------
-- Function used to get a video key by passing as argument a video name
-- If the movie key is not found function return NULL
-----------------------------------------------------------------------
CREATE OR REPLACE FUNCTION GetVideoKeyByName(inVideoName TEXT)
RETURNS INT LANGUAGE plpgsql
AS $function$
    BEGIN
        return (SELECT (Video_Key) FROM Video WHERE Title = inVideoName);
    END;
$function$;

-------------------------------------------------------------------------
-- Function used to insert an actor into ACTORS table for a certain video
-------------------------------------------------------------------------
CREATE OR REPLACE FUNCTION mspublic.AddActor(inMovieName TEXT, inActorName TEXT)
RETURNS INTEGER LANGUAGE plpgsql
AS $function$
    DECLARE
        vActor_Key INTEGER;
        vPeople_Key INTEGER;
        vVideo_Key INTEGER;
    BEGIN
        SELECT mspublic.GetVideoKeyByName(inMovieName) INTO vVideo_Key;
        IF vVideo_Key IS NOT NULL AND inActorName IS NOT NULL THEN
            SELECT mspublic.GetPeopleKeyByName(inActorName) INTO vPeople_Key;
            IF vPeople_Key IS NULL THEN
                SELECT mspublic.AddPeople(inActorName, NULL, 1) INTO vPeople_Key;
            END IF;         
            IF vPeople_Key IS NOT NULL THEN
                SELECT nextval('mspublic.actors_actor_key_seq'::regclass) INTO vActor_Key;
                INSERT INTO ACTORS(Actor_Key, People_Key, Video_Key)
                VALUES
                (
                    vActor_Key,
                    vPeople_Key,
                    vVideo_Key
                );
                return vActor_Key;
            ELSE
                return NULL;
            END IF;
        ELSE
            return NULL;
        END IF;
    END;
$function$;

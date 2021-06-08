SELECT mspublic.AddVideoGenre('La munte', 1);


SELECT mspublic.AddSeries('Seinfeld3', NULL, 'cool movie ;)', TRUE, 1, 9.0,
                          '01 Jan 2012', TRUE, FALSE,
                          7.1, 'www.omdb.com/sunshine', NULL, NULL, 1, 45, 11, FALSE, 'USA', 'English');




DELETE FROM mspublic.actors;
DELETE FROM mspublic.people;
DELETE FROM mspublic.video_genres;
DELETE FROM mspublic.video_language;
DELETE FROM mspublic.video_country;
DELETE FROM mspublic.extra_video_info_doc;
DELETE FROM mspublic.extra_video_info_series;
DELETE FROM mspublic.video;   


SELECT (title) FROM mspublic.Video WHERE imdb_score > 8.8;

SELECT mspublic.GetMovieKeyByName('La munte');

SELECT mspublic.AddSeriesActor('La mare', 'Arnold Schwarzenegger');

SELECT mspublic.AddPeople('Arnold Schwarzenegger2', 'American', 1);

SELECT mspublic.AddPeople('Danny DeVito', 'American', 1);

SELECT mspublic.AddNewStorage('hdd-01', 1, TRUE, '01 Jun 2012', FALSE, NULL);
SELECT mspublic.AddNewStorage('hdd-02', 1, TRUE, '01 Jun 2012', FALSE, NULL);
SELECT mspublic.AddNewStorage('hdd-03', 1, TRUE, '01 Jun 2012', FALSE, NULL);
SELECT mspublic.AddNewStorage('dvd', 1, TRUE, '01 Jun 2012', FALSE, NULL);
SELECT mspublic.AddNewStorage('steam', 1, TRUE, '01 Jun 2012', FALSE, NULL);
SELECT mspublic.AddNewStorage('gog', 1, TRUE, '01 Jun 2012', FALSE, NULL);


SELECT mspublic.addvideo('American', NULL, 'cool movie ;)', 1, TRUE, 1, 9.0, 
                         '01 Jun 2012', TRUE, FALSE, 120, 
                         7.1, 'www.omdb.com/sunshine', NULL, NULL, 1);
                         


SELECT mspublic.CreateBackupLink('HDD-01', 'HDD-03');




SELECT (Title, imdb_score) FROM video
JOIN (SELECT (video_key) FROM Actors 
      JOIN People ON (Actors.people_key = People.people_key)
      AND People.name = 'Roberto Benigni') AS VK
ON VK.video_key = video.video_key
ORDER BY imdb_score DESC;



SELECT (Title, imdb_score) FROM video
JOIN (SELECT (video_key) FROM video_language WHERE LANGUAGE = 'Japanese') AS VK
ON VK.video_key = video.video_key
ORDER BY imdb_score DESC;


-- If we are searching for a movie
SELECT (Title) FROM video 
JOIN movies ON video.video_key = movies.video_key
AND Title LIKE '%American%';

-- If we are searching for a doc
SELECT (Title) FROM video 
JOIN documentaries ON video.video_key = documentaries.video_key
AND Title LIKE '%American%';

-- If we are searching for a series
SELECT (Title) FROM video 
JOIN series ON video.video_key = series.video_key
AND Title LIKE '%American%'; 

-- If we are searching for an other video
SELECT (Title) FROM video 
JOIN other_videos ON video.video_key = other_videos.video_key
AND Title LIKE '%American%'; 


SELECT (Title) FROM video WHERE Title LIKE '%Amer%'; 
SELECT (Title) FROM video WHERE Title = 'American';  -- exact version





SELECT  Title, Also_Known_As, Description, Video_Type, Saw_It, Quality, Personal_Score, Release_Date, Has_Romanian_Sub, Has_English_Sub, Runtime, Added_To_Db_Date, Imdb_Score, Omdb_Link, Imdb_Link, Youtube_Link 
FROM mspublic.video 

-- storage part
JOIN (SELECT (storage_key) FROM mspublic.storage WHERE storage_name = 'HDD-01') AS SK
ON SK.storage_key = mspublic.video.storage_key

-- country part
JOIN (SELECT (video_key) FROM mspublic.video_country WHERE country = 'Romania') AS VK
ON VK.video_key = video.video_key

-- title part
AND Title  LIKE '%American%';

SELECT * FROM pg_stat_activity;

SELECT pg_terminate_backend(pg_stat_activity.pid)
FROM pg_stat_activity
WHERE pg_stat_activity.datname = 'multimediaspot'
  AND pid <> pg_backend_pid();


DO $$
DECLARE
    video_key INTEGER;
BEGIN
    SELECT mspublic.GenerateNextVideoKey() INTO video_key;
    INSERT INTO MSPUBLIC.VIDEO (Video_key, Title, Also_Known_As, Description, 
                                Saw_It, Quality, Personal_Score, Release_Date, 
                                Has_Romanian_Sub, Has_English_Sub, Runtime, Added_To_Db_Date, 
                                Imdb_Score, Omdb_Link, Imdb_Link, Youtube_Link, Storage_Key) VALUES
    (
            video_key,
            'Title 03',
            'Title 02',
            'A small description',
            TRUE,
            1,
            10,
            to_date('10 Dec 1990', 'DD Mon YYYY'),
            TRUE,
            TRUE,
            120,
            now()::date,
            9.0,
            NULL,
            NULL,
            NULL,
            1
    );
END $$;
package org.projects.centralpoint.Extern.Fetching;



public interface VideoDataFetcher
{
    enum IdType
    {
        IMDB,
        TMDB
    }

    enum PosterSize
    {
        BIG,
        SMALL
    }

    Response getMovie(String id, IdType type);
    Response getMovie(String movieTitle, String movieYear);

    Response getSeries(String id, IdType type);
    Response getSeries(String seriesTitle, String seriesYear);

    Response getPerson(String id, IdType type);
    Response getPerson(String personName);

    Response getPoster(String id, PosterSize ps);
}

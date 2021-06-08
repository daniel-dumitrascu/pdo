package org.projects.centralpoint.Utils.Web;

import java.util.ArrayList;
import java.util.List;

enum TmdbVideoType
{
    TMDB_MOVIE,
    TMDB_SERIES
}

enum TmdbPosterSize
{
    TMDB_W92,
    TMDB_W154,
    TMDB_W185,
    TMDB_W342,
    TMDB_W500,
    TMDB_W780,
    TMDB_ORIGINAL
}

public class TmdbWebRequestsCreator
{

    public String getSearchByTitleLink()
    {
        String link = null;
        if(videoTitle != null && !videoTitle.isEmpty())
        {
            // We will start by adding the base link
            link = "https://api.themoviedb.org";
            link += "/3";

            // Add the methods
            if(videoType == TmdbVideoType.TMDB_MOVIE)
            {
                link += "/search/movie";
            }
            else if(videoType == TmdbVideoType.TMDB_SERIES)
            {
                link += "/search/series";
            }

            // Add the API key
            link += "?api_key=" + apiKey;

            // Add language
            link += "&language=en-US";

            // Add year if available
            link += videoYear.isEmpty() ? "" : "&language=en-US";

            // Handle special characters in the object title then add it to the final link
            String title = videoTitle;
            title = title.replaceAll("\\s+", " ");
            title = title.replace(" ", "+");

            link += "&query=" + title;
        }

        return link;
    }

    public String getSearchByIdLink()
    {
        String link = null;
        if(videoId != null && !videoId.isEmpty())
        {
            // We will start by adding the base link
            link = "https://api.themoviedb.org";
            link += "/3";

            // Add the methods
            if(videoType == TmdbVideoType.TMDB_MOVIE)
            {
                link += "/movie";
            }
            else if(videoType == TmdbVideoType.TMDB_SERIES)
            {
                link += "/series";
            }

            // Add the ID
            link += "/" + videoId;

            // Add the API key
            link += "?api_key=" + apiKey;
        }

        return link;
    }

    public String getCastLink()
    {
        String link = null;
        if(videoId != null && !videoId.isEmpty())
        {
            // We will start by adding the base link
            link = "https://api.themoviedb.org";
            link += "/3";

            // Add the methods
            if(videoType == TmdbVideoType.TMDB_MOVIE)
            {
                link += "/movie";
            }
            else if(videoType == TmdbVideoType.TMDB_SERIES)
            {
                link += "/series";
            }

            // Add the ID
            link += "/" + videoId;

            // Add the method name
            link += "/credits";

            // Add the API key
            link += "?api_key=" + apiKey;
        }

        return link;
    }

    public String getPosterLink(TmdbPosterSize posterSize)
    {
        String link = null;
        if(videoPosterPath != null && !videoPosterPath.isEmpty())
        {
            // We will start by adding the base link
            link = "https://image.tmdb.org/t/p";

            // Add the sizing
            String strPosterSize = getPosterSize(posterSize);
            if(strPosterSize != null && !strPosterSize.isEmpty())
            {
                link += strPosterSize;
            }
            else
            {
                return null;
            }

            // Add the poster path
            link += "/" + videoPosterPath;
        }

        return link;
    }

    public void setVideoTitle(String title, TmdbVideoType type)
    {
        videoTitle = title;
        videoType = type;
    }

    public void setVideoId(String id)
    {
        videoId = id;
    }

    public void setVideoYear(String year)
    {
        videoYear = year;
    }

    public void clear()
    {
        videoTitle = "";
        videoYear = "";
        videoType = TmdbVideoType.TMDB_MOVIE;
        videoId = "";
        videoPosterPath = "";
    }

    private String getPosterSize(TmdbPosterSize posterSize)
    {
        String result = null;
        if(posterSize == TmdbPosterSize.TMDB_W92)
        {
            result = "/w92";
        }
        else if(posterSize == TmdbPosterSize.TMDB_W154)
        {
            result = "/w154";
        }
        else if(posterSize == TmdbPosterSize.TMDB_W185)
        {
            result = "/w185";
        }
        else if(posterSize == TmdbPosterSize.TMDB_W342)
        {
            result = "/w342";
        }
        else if(posterSize == TmdbPosterSize.TMDB_W500)
        {
            result = "/w500";
        }
        else if(posterSize == TmdbPosterSize.TMDB_W780)
        {
            result = "/w780";
        }
        else if(posterSize == TmdbPosterSize.TMDB_ORIGINAL)
        {
            result = "/original";
        }

        return result;
    }

    // Input information
    private String videoTitle = new String();
    private String videoYear = new String();
    private TmdbVideoType videoType = TmdbVideoType.TMDB_MOVIE;

    // Collected information from TMDB
    private String videoId = new String();
    private String videoPosterPath = new String();

    // Constant information
    private String apiKey = "3eddce8d99df267a4725c1a5e6e3eb7d";
}

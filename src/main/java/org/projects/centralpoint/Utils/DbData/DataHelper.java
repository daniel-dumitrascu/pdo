package org.projects.centralpoint.Utils.DbData;

import org.projects.centralpoint.Defines.VideoGenres;
import org.projects.centralpoint.Defines.WorldCountries;

public class DataHelper
{
    public static String getFormattedGenre(String strGenre)
    {
        String result = "";

        if(strGenre != null && !strGenre.isEmpty())
        {
            if(strGenre.equalsIgnoreCase("Action"))
                result = VideoGenres.getGenreByIndex(VideoGenres.GenreIndex.ACTION);
            else if(strGenre.equalsIgnoreCase("Adventure"))
                result = VideoGenres.getGenreByIndex(VideoGenres.GenreIndex.ADVENTURE);
            else if(strGenre.equalsIgnoreCase("Animation"))
                result = VideoGenres.getGenreByIndex(VideoGenres.GenreIndex.ANIMATION);
            else if(strGenre.equalsIgnoreCase("Comedy"))
                result = VideoGenres.getGenreByIndex(VideoGenres.GenreIndex.COMEDY);
            else if(strGenre.equalsIgnoreCase("Crime"))
                result = VideoGenres.getGenreByIndex(VideoGenres.GenreIndex.CRIME);
            else if(strGenre.equalsIgnoreCase("Drama"))
                result = VideoGenres.getGenreByIndex(VideoGenres.GenreIndex.DRAMA);
            else if(strGenre.equalsIgnoreCase("Fantasy"))
                result = VideoGenres.getGenreByIndex(VideoGenres.GenreIndex.FANTASY);
            else if(strGenre.equalsIgnoreCase("Horror"))
                result = VideoGenres.getGenreByIndex(VideoGenres.GenreIndex.HORROR);
            else if(strGenre.equalsIgnoreCase("Mystery"))
                result = VideoGenres.getGenreByIndex(VideoGenres.GenreIndex.MYSTERY);
            else if(strGenre.equalsIgnoreCase("Romance"))
                result = VideoGenres.getGenreByIndex(VideoGenres.GenreIndex.ROMANCE);
            else if(strGenre.equalsIgnoreCase("Sci-Fi") ||
                    strGenre.equalsIgnoreCase("SF") ||
                    strGenre.equalsIgnoreCase("Science Fiction"))
                result = VideoGenres.getGenreByIndex(VideoGenres.GenreIndex.SF);
            else if(strGenre.equalsIgnoreCase("Thriller"))
                result = VideoGenres.getGenreByIndex(VideoGenres.GenreIndex.THRILLER);
            else if(strGenre.equalsIgnoreCase("Western"))
                result = VideoGenres.getGenreByIndex(VideoGenres.GenreIndex.WESTERN);
            else if(strGenre.equalsIgnoreCase("Biography"))
                result = VideoGenres.getGenreByIndex(VideoGenres.GenreIndex.BIOGRAPHY);
            else if(strGenre.equalsIgnoreCase("History"))
                result = VideoGenres.getGenreByIndex(VideoGenres.GenreIndex.HISTORY);
            else if(strGenre.equalsIgnoreCase("War"))
                result = VideoGenres.getGenreByIndex(VideoGenres.GenreIndex.WAR);
        }

        return result;
    }

    public static String getFormattedCountry(String strCountry)
    {
        String country = strCountry.toLowerCase();
        if(WorldCountries.isValidCountry(country))
            return strCountry;

        return "";
    }
}

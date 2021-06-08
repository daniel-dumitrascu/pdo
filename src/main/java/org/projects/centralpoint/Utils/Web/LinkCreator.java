package org.projects.centralpoint.Utils.Web;

import java.time.LocalDate;
import java.net.URLEncoder;

//TODO indications on how to change this classes
//1. As interface, this needs to have only one method: CreateLink
//   This will take an enum of type LINK_TYPE { IMDB, YOUTUBE, OMDB } and other params that we need
//2. The links construction implementation for different sites (omdb or imdb) it will be done in separated classes
//   which derive from a base class
public class LinkCreator
{
    // TODO I have 3 methods with the same implementation, duplicated code
    // the duplicated code must stay in a common method
    public static String ConstructLinkForOMDB(String movieTitle)
    {
        String link = new String();

        // Handle special characters in the object title
        movieTitle = movieTitle.replaceAll("\\s+", " ");
        movieTitle = movieTitle.replace(" ", "+");

        // Use the movie title in the link construction
        link = "http://" + m_omdbSiteLink;
        link += "/?t=";
        link += movieTitle;

        // We want to get the full plot
        link += "&plot=full";

        // Add the private key
        link += "&apikey=" + m_omdbPrivateKey;

        return link;
    }

    public static String ConstructLinkForOMDB(String movieTitle, String movieYear)
    {
        String link = new String();

        // Handle special characters in the object title
        movieTitle = movieTitle.replaceAll("\\s+", " ");
        movieTitle = movieTitle.replace(" ", "+");

        // Add the movie title
        link = "http://" + m_omdbSiteLink;
        link += "/?t=";
        link += movieTitle;

        // Add the movie year
        link += "&y=";
        link += movieYear;

        // We want to get the full plot
        link += "&plot=full";

        // Add the private key
        link += "&apikey=" + m_omdbPrivateKey;

        return link;
    }

    public static String ConstructLinkForIMDB(String movieID)
    {
        String link = new String();
        link = "http://" + m_imdbSiteLink;
        link += "/title/";
        link += movieID;
        link += "/";

        return link;
    }

    // TODO this members will stay inside of the derived classes which construct the link
    // and not here because here (in LinkCreator) I don't care how the link to OMDB or IMDB is obtained
    // I don't care about the implementation ...
    private static final String m_omdbSiteLink = "www.omdbapi.com";
    private static final String m_imdbSiteLink = "www.imdb.com";
    private static final String m_omdbPrivateKey = "91fe8efe";
}

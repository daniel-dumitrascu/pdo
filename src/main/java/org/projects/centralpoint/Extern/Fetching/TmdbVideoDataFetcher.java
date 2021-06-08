package org.projects.centralpoint.Extern.Fetching;

import com.google.gson.*;
import org.apache.commons.io.FilenameUtils;
import org.projects.centralpoint.Defines.Types;
import org.projects.centralpoint.Defines.VideoGenres;
import org.projects.centralpoint.Utils.Web.RequestHeaderDetails;
import org.projects.centralpoint.Utils.Web.SessionLimiter;
import org.projects.centralpoint.Utils.Web.WebImageDlg;
import org.projects.centralpoint.Utils.Web.WebJsonDld;
import org.projects.centralpoint.File.Loader.Cell;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TmdbVideoDataFetcher implements VideoDataFetcher
{
    public TmdbVideoDataFetcher(String apiKey, int apiVersion)
    {
        this.tmdbApiKey = apiKey;
        this.tmdbApiVersion = apiVersion;

        webJsonDlg = new WebJsonDld();
        webImageDlg = new WebImageDlg();
        dataToFetch = new ArrayList<Cell>();

        dataToFetch.add(new Cell("imdb_id", "String", null));
        dataToFetch.add(new Cell("title", "String", null));
        dataToFetch.add(new Cell("overview", "String", null));
        dataToFetch.add(new Cell("poster_path", "String", null));
        dataToFetch.add(new Cell("release_date", "String", null));
        dataToFetch.add(new Cell("revenue", "Integer", null));
        dataToFetch.add(new Cell("budget", "Integer", null));
        dataToFetch.add(new Cell("runtime", "Integer", null));
        dataToFetch.add(new Cell("status", "String", null));
        dataToFetch.add(new Cell("vote_average", "Float", null));
        dataToFetch.add(new Cell("vote_count", "Integer", null));
    }

    public Response getMovie(String id, IdType type)
    {
        Response response = new Response();

        if(type == IdType.IMDB)
        {
            // In the case of an IMDB id we should fetch the TMDB id first
            // and then get the rest of the movie details
            response = getTmdbIdFromImdbId(id);
        }
        else if(type == IdType.TMDB)
        {
            response.setData(id);
            response.setResponseStatus(Response.ResponseStatus.FETCH_OK);
        }

        if(response.getResponseStatus() == Response.ResponseStatus.FETCH_OK)
        {
            response = getVideoDetails((String)response.getData(), VideoType.MOVIE);
        }

        return response;
    }

    public Response getMovie(String movieTitle, String movieYear)
    {
        Response response = findVideo(movieTitle, movieYear, VideoType.MOVIE);
        if(response.getResponseStatus() == Response.ResponseStatus.FETCH_OK)
        {
            response = getMovie((String)response.getData(), IdType.TMDB);
        }

        return response;
    }

    public Response getSeries(String id, IdType type)
    {
        Response response = new Response();

        if(type == IdType.IMDB)
        {
            // In the case of an IMDB id we should fetch the TMDB id first
            // and then get the rest of the movie details
            response = getTmdbIdFromImdbId(id);
        }
        else if(type == IdType.TMDB)
        {
            response.setData(id);
            response.setResponseStatus(Response.ResponseStatus.FETCH_OK);
        }

        if(response.getResponseStatus() == Response.ResponseStatus.FETCH_OK)
        {
            response = getVideoDetails((String)response.getData(), VideoType.SERIES);
        }

        return response;
    }

    public Response getSeries(String seriesTitle, String seriesYear)
    {
        Response response = findVideo(seriesTitle, seriesYear, VideoType.SERIES);
        if(response.getResponseStatus() == Response.ResponseStatus.FETCH_OK)
        {
            response = getSeries((String)response.getData(), IdType.TMDB);
        }

        return response;
    }

    public Response getPerson(String id, IdType type)
    {
        Response response = new Response();

        if(type == IdType.IMDB)
        {
            //TODO To implement
        }
        else if(type == IdType.TMDB)
        {
            response.setData(id);
            response.setResponseStatus(Response.ResponseStatus.FETCH_OK);
        }

        if(response.getResponseStatus() == Response.ResponseStatus.FETCH_OK)
        {
            response = getPersonDetails((String)response.getData());
        }

        return response;
    }

    public Response getPerson(String personName)
    {
        Response response = getIdOfPerson(personName);
        return getPersonDetails((String)response.getData());
    }

    public Response getPoster(String id, PosterSize ps)
    {
        Response response = null;
        if(ps == PosterSize.BIG)
        {
            response = getBigPoster(id);
        }
        else
        {
            response = getSmallPoster(id);
        }

        return response;
    }

    private Response getBigPoster(String id)
    {
        Response response = new Response();
        sessionLimiter.setRequestsChunk(1);

        if(sessionLimiter.canMakeRequests() && id != null && !id.isEmpty())
        {
            String requestURL = "https://image.tmdb.org/t/p/";
            requestURL += "w500/";
            requestURL += id;

            webImageDlg.Load(requestURL);
            if(webImageDlg.IsLoaded())
            {
                Path p = Paths.get(id);

                String extension = FilenameUtils.getExtension(id);
                String fileName = FilenameUtils.removeExtension(p.getFileName().toString());

                //TODO size id incorect
                PosterData pd = new PosterData(fileName,
                                               6,
                                               7,
                                               extension,
                                               (InputStream) webImageDlg.GetPageBody());
                response.setData(pd);
                response.setResponseStatus(Response.ResponseStatus.FETCH_OK);
            }
        }
        return response;
    }

    private Response getSmallPoster(String id)
    {
        return null;
    }

    private Response getIdOfPerson(String personName)
    {
        Response response = new Response();
        sessionLimiter.setRequestsChunk(1);
        String personId = null;

        if(sessionLimiter.canMakeRequests())
        {
            response.setCooldownTime(sessionLimiter.getCooldownTime());

            String requestURL = tmdbBaseURL;
            requestURL += Integer.toString(tmdbApiVersion) + "/";
            requestURL += "search/person";
            requestURL += "?api_key=" + tmdbApiKey;
            requestURL += "&language=en-US";
            requestURL += "&query=" + personName;

            webJsonDlg.Load(requestURL);
            if(webJsonDlg.IsLoaded())
            {
                String body = webJsonDlg.GetPageBody();
                RequestHeaderDetails header = webJsonDlg.GetPageHeader();

                if(header.getRateLimitRemaining() != null &&
                        header.getRateLimitReset() != null)
                {
                    sessionLimiter.update(header.getRateLimitRemaining(), header.getRateLimitReset());
                }

                if(!IsResponseValid(body))
                    return null;

                JsonElement jelement = new JsonParser().parse(body);
                JsonObject  jobject = jelement.getAsJsonObject();

                if( jobject != null &&
                   !jobject.get("total_results").isJsonNull() &&
                    jobject.get("total_results").getAsInt() > 0 )
                {
                    JsonArray jPeopleArray = jobject.getAsJsonArray("results");
                    for(int i = 0; i < jPeopleArray.size(); ++i)
                    {
                        JsonObject jobj = jPeopleArray.get(i).getAsJsonObject();
                        JsonElement jeId = jobj.get("id");
                        if(jeId.isJsonNull())
                            continue;

                        JsonElement jeName = jobj.get("name");
                        if(jeName.isJsonNull())
                            continue;

                        if(personName.equalsIgnoreCase(jeName.getAsString()))
                            personId = Integer.toString(jeId.getAsInt());

                        // As soon as we have a match we exit the loop
                        break;
                    }

                    response.setData(personId);
                    response.setResponseStatus(Response.ResponseStatus.FETCH_OK);
                }
            }
        }
        else
        {
            response.setCooldownTime(sessionLimiter.getCooldownTime());
            response.setResponseStatus(Response.ResponseStatus.FETCH_LIMIT_EXCEEDED);
        }

        return response;
    }

    private Response getTmdbIdFromImdbId(String imdbId)
    {
        Response response = new Response();
        sessionLimiter.setRequestsChunk(1);
        String tmdbId = null;

        if(sessionLimiter.canMakeRequests())
        {
            response.setCooldownTime(sessionLimiter.getCooldownTime());

            String requestURL = tmdbBaseURL;
            requestURL += Integer.toString(tmdbApiVersion) + "/";
            requestURL += "find/";
            requestURL += imdbId;
            requestURL += "?api_key=" + tmdbApiKey;
            requestURL += "&language=en-US";
            requestURL += "&external_source=imdb_id";

            webJsonDlg.Load(requestURL);
            if(webJsonDlg.IsLoaded())
            {
                String responce = webJsonDlg.GetPageBody();
                RequestHeaderDetails header = webJsonDlg.GetPageHeader();

                if(header.getRateLimitRemaining() != null &&
                        header.getRateLimitReset() != null)
                {
                    sessionLimiter.update(header.getRateLimitRemaining(), header.getRateLimitReset());
                }

                if(!IsResponseValid(responce))
                {
                    return null;
                }

                JsonElement jelement = new JsonParser().parse(responce);
                JsonObject  jobject = jelement.getAsJsonObject();
                JsonArray jgenresArray = jobject.getAsJsonArray("movie_results");
                for(int i = 0; i < jgenresArray.size(); ++i)
                {
                    JsonObject jobj = jgenresArray.get(i).getAsJsonObject();
                    JsonElement je = jobj.get("id");
                    if(je.isJsonNull())
                        continue;
                    tmdbId = Integer.toString(je.getAsInt());

                    // As soon as we have a match we exit the loop
                    break;
                }

                response.setData(tmdbId);
                response.setResponseStatus(Response.ResponseStatus.FETCH_OK);
            }
        }
        else
        {
            response.setCooldownTime(sessionLimiter.getCooldownTime());
            response.setResponseStatus(Response.ResponseStatus.FETCH_LIMIT_EXCEEDED);
        }

        return response;
    }

    private Response getVideoDetails(String id, VideoType videoType)
    {
        Response response = new Response();
        sessionLimiter.setRequestsChunk(1);

        if(sessionLimiter.canMakeRequests())
        {
            String type = "";

            if(videoType == VideoType.MOVIE) type = "movie/";
            else type = "tv/";

            String requestURL = tmdbBaseURL;
            requestURL += Integer.toString(tmdbApiVersion) + "/";
            requestURL += type;
            requestURL += id;
            requestURL += "?api_key=" + tmdbApiKey;
            requestURL += "&language=en-US";
            requestURL += "&append_to_response=credits";

            webJsonDlg.Load(requestURL);
            String body = webJsonDlg.GetPageBody();
            RequestHeaderDetails header = webJsonDlg.GetPageHeader();

            if(header.getRateLimitRemaining() != null &&
               header.getRateLimitReset() != null)
            {
                sessionLimiter.update(header.getRateLimitRemaining(), header.getRateLimitReset());
            }

            if(!IsResponseValid(body))
            {
                response.setResponseStatus(Response.ResponseStatus.FETCH_FAIL);
                return response;
            }

            VideoData videoData = new VideoData();

            JsonElement jelement = new JsonParser().parse(body);
            JsonObject  jobject = jelement.getAsJsonObject();
            videoData.setTmdbId(Integer.parseInt(id));

            for(Cell cell : dataToFetch)
            {
                if(!jobject.get(cell.GetCellTitle()).isJsonNull())
                {
                    Object value = null;

                    try
                    {
                        if(cell.GetCellType().equals("String"))
                        {
                            value = jobject.get(cell.GetCellTitle()).getAsString();
                        }
                        else if(cell.GetCellType().equals("Integer"))
                        {
                            value = jobject.get(cell.GetCellTitle()).getAsInt();
                        }
                        else if(cell.GetCellType().equals("Float"))
                        {
                            value = jobject.get(cell.GetCellTitle()).getAsFloat();
                        }
                        else
                        {
                            continue;
                        }
                    }
                    catch(java.lang.ClassCastException ex)
                    {
                        //TODO log an error message
                        continue;
                    }

                    if(cell.GetCellTitle().equals("imdb_id")) { videoData.setImdbId((String)value); }
                    else if(cell.GetCellTitle().equals("title")) { videoData.setTitle((String)value); }
                    else if(cell.GetCellTitle().equals("overview")) { videoData.setOverview((String)value); }
                    else if(cell.GetCellTitle().equals("poster_path")) { videoData.setPosterPath((String)value); }
                    else if(cell.GetCellTitle().equals("release_date")) { videoData.setReleaseDate((String)value); }
                    else if(cell.GetCellTitle().equals("revenue")) { videoData.setRevenue((Integer)value); }
                    else if(cell.GetCellTitle().equals("budget")) { videoData.setBudget((Integer)value); }
                    else if(cell.GetCellTitle().equals("runtime")) { videoData.setRuntime((Integer)value); }
                    else if(cell.GetCellTitle().equals("status")) { videoData.setStatus((String)value); }
                    else if(cell.GetCellTitle().equals("vote_average")) { videoData.setVoteAverage((Float)value); }
                    else if(cell.GetCellTitle().equals("vote_count")) { videoData.setVoteCount((Integer)value); }
                }
            }

            /*
             * Fetch the genres as well
             */
            JsonArray jgenresArray = jobject.getAsJsonArray("genres");
            List<String> genres = new ArrayList<String>();
            for(int i = 0; i < jgenresArray.size(); ++i)
            {
                JsonObject jobj = jgenresArray.get(i).getAsJsonObject();
                JsonElement je = jobj.get("name");
                if(je.isJsonNull())
                    continue;
                genres.add(je.getAsString());
            }

            videoData.setGenres(genres);

            /*
             * Fetch the languages
             */
            JsonArray jlangArray = jobject.getAsJsonArray("spoken_languages");
            List<String> languages = new ArrayList<String>();
            for(int i = 0; i < jlangArray.size(); ++i)
            {
                JsonObject jobj = jlangArray.get(i).getAsJsonObject();
                JsonElement je = jobj.get("name");
                if(je.isJsonNull())
                    continue;
                languages.add(je.getAsString());
            }

            videoData.setLanguages(languages);

            /*
             * Fetch the production countries
             */
            JsonArray jcountryArray = jobject.getAsJsonArray("production_countries");
            List<String> countries = new ArrayList<String>();
            for(int i = 0; i < jcountryArray.size(); ++i)
            {
                JsonObject jobj = jcountryArray.get(i).getAsJsonObject();
                JsonElement je = jobj.get("name");
                if(je.isJsonNull())
                    continue;
                countries.add(je.getAsString());
            }

            videoData.setCountries(countries);

            /*
             * Fetch the actors
             */
            List<PersonData> actors = new ArrayList<PersonData>();
            JsonObject jcreditsObject = jobject.getAsJsonObject("credits");
            JsonArray jcreditArray = jcreditsObject.getAsJsonArray("cast");
            for(int i = 0; i < jcreditArray.size(); ++i)
            {
                JsonObject jobj = jcreditArray.get(i).getAsJsonObject();
                PersonData p = new PersonData();

                /*
                 * Set the name
                 */
                JsonElement je = jobj.get("name");
                if(je != null && je.isJsonNull())
                    continue;
                p.setName(je.getAsString());

                /*
                 * Set the birthday
                 */
                //TODO
                /*je = jobj.get("birthday");
                LocalDate birthdayDate = null;
                if(je != null && !je.isJsonNull())
                {
                    String stringBirthdayDate = je.getAsString();

                    try
                    {
                        if(stringBirthdayDate != null)
                        {
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-d");
                            formatter = formatter.withLocale(Locale.UK);
                            birthdayDate = LocalDate.parse(stringBirthdayDate, formatter);
                        }
                    }
                    catch(Exception e)
                    {
                        birthdayDate = null;
                    }
                }

                p.setBirthday(birthdayDate);*/

                /*
                 * Set the deathday
                 */
                /*je = jobj.get("deathday");
                LocalDate deathdayDate = null;
                if(je != null && !je.isJsonNull())
                {
                    String stringDeathdayDate = je.getAsString();

                    try
                    {
                        if(stringDeathdayDate != null)
                        {
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-d");
                            formatter = formatter.withLocale(Locale.UK);
                            deathdayDate = LocalDate.parse(stringDeathdayDate, formatter);
                        }
                    }
                    catch(Exception e)
                    {
                        deathdayDate = null;
                    }
                }

                p.setDeathday(deathdayDate);*/

                /*
                 * Set the id
                 */
                je = jobj.get("id");
                if(je != null && je.isJsonNull())
                    continue;
                p.setId(je.getAsInt());

                /*
                 * Set the genre
                 */
                //TODO
                /*je = jobj.get("gender");
                if(je != null && !je.isJsonNull())
                {
                    int gender = je.getAsInt();
                    if(gender == 1) p.setGender(Types.PeopleGender.FEMALE);
                    else if(gender == 2) p.setGender(Types.PeopleGender.MALE);
                }*/

                /*
                 * Set the person image path
                 */
                /*je = jobj.get("profile_path");
                if(je != null && !je.isJsonNull())
                {
                    p.setImagePath(je.getAsString());
                }*/

                actors.add(p);
            }

            videoData.setActors(actors);
            response.setData(videoData);
            response.setResponseStatus(Response.ResponseStatus.FETCH_OK);
        }
        else
        {
            response.setCooldownTime(sessionLimiter.getCooldownTime());
            response.setResponseStatus(Response.ResponseStatus.FETCH_LIMIT_EXCEEDED);
        }

        return response;
    }

    private Response getPersonDetails(String id)
    {
        Response response = new Response();
        sessionLimiter.setRequestsChunk(1);

        if(sessionLimiter.canMakeRequests())
        {
            String requestURL = tmdbBaseURL;
            requestURL += Integer.toString(tmdbApiVersion) + "/";
            requestURL += "person/";
            requestURL += id;
            requestURL += "?api_key=" + tmdbApiKey;
            requestURL += "&language=en-US";

            webJsonDlg.Load(requestURL);
            String body = webJsonDlg.GetPageBody();
            RequestHeaderDetails header = webJsonDlg.GetPageHeader();

            if(header.getRateLimitRemaining() != null && header.getRateLimitReset() != null)
            {
                sessionLimiter.update(header.getRateLimitRemaining(), header.getRateLimitReset());
            }

            if(!IsResponseValid(body))
            {
                response.setResponseStatus(Response.ResponseStatus.FETCH_FAIL);
                return response;
            }

            PersonData personData = new PersonData();

            JsonElement jelement = new JsonParser().parse(body);
            JsonObject  jobject = jelement.getAsJsonObject();
            personData.setId(Integer.parseInt(id));

            /*
             * Set the birthday
             */
            if(jobject != null && !jobject.get("birthday").isJsonNull())
            {
                String birthday = jobject.get("birthday").getAsString();
                LocalDate birthdayDate = null;

                try
                {
                    if(birthday != null)
                    {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-d");
                        formatter = formatter.withLocale(Locale.UK);
                        birthdayDate = LocalDate.parse(birthday, formatter);
                    }
                }
                catch(Exception e)
                {
                    birthdayDate = null;
                }

                personData.setBirthday(birthdayDate);
            }

            /*
             * Set the place of birth
             */
            if(jobject != null && !jobject.get("place_of_birth").isJsonNull())
            {
                String placeOfBirth = jobject.get("place_of_birth").getAsString();
                personData.setPlaceOfBirth(placeOfBirth);
            }

            /*
             * Set the deathday
             */
            if(jobject != null && !jobject.get("deathday").isJsonNull())
            {
                String deathday = jobject.get("deathday").getAsString();
                LocalDate deathdayDate = null;

                try
                {
                    if(deathday != null)
                    {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-d");
                        formatter = formatter.withLocale(Locale.UK);
                        deathdayDate = LocalDate.parse(deathday, formatter);
                    }
                }
                catch(Exception e)
                {
                    deathdayDate = null;
                }

                personData.setDeathday(deathdayDate);
            }

            /*
             * Set the name
             */
            if(jobject != null && !jobject.get("name").isJsonNull())
            {
                String name = jobject.get("name").getAsString();
                personData.setName(name);
            }

            /*
             * Set the gender
             */
            if(jobject != null && !jobject.get("gender").isJsonNull())
            {
                int genderInx = jobject.get("gender").getAsInt();
                personData.setGender(genderInx == 1 ? Types.PeopleGender.FEMALE : Types.PeopleGender.MALE);
            }

            /*
             * Set the biography
             */
            if(jobject != null && !jobject.get("biography").isJsonNull())
            {
                String biography = jobject.get("biography").getAsString();
                personData.setBiography(biography);
            }

            /*
             * Set the profile photo
             */
            if(jobject != null && !jobject.get("profile_path").isJsonNull())
            {
                String profilePath = jobject.get("profile_path").getAsString();
                personData.setImagePath(profilePath);
            }

            response.setData(personData);
            response.setResponseStatus(Response.ResponseStatus.FETCH_OK);
        }
        else
        {
            response.setCooldownTime(sessionLimiter.getCooldownTime());
            response.setResponseStatus(Response.ResponseStatus.FETCH_LIMIT_EXCEEDED);
        }

        return response;
    }

    private Response findVideo(String videoTitle, String videoYear, VideoType videoType)
    {
        Response response = new Response();
        sessionLimiter.setRequestsChunk(1);
        String tmdbId = null;

        if(sessionLimiter.canMakeRequests())
        {
            String type = "";

            if(videoType == VideoType.MOVIE) type = "movie/";
            else type = "tv/";

            // Handle special characters in the object title
            String modifiedVideoTitle = videoTitle.replaceAll("\\s+", " ");
            modifiedVideoTitle = modifiedVideoTitle.replace(" ", "+");

            String requestURL = tmdbBaseURL;
            requestURL += Integer.toString(tmdbApiVersion) + "/";
            requestURL += "search/";
            requestURL += type;
            requestURL += "?api_key=" + tmdbApiKey;
            requestURL += "&language=en-US";
            requestURL += "&query=" + modifiedVideoTitle;
            requestURL += "&year=" + videoYear;

            webJsonDlg.Load(requestURL);
            String responce = webJsonDlg.GetPageBody();
            RequestHeaderDetails header = webJsonDlg.GetPageHeader();

            if(header.getRateLimitRemaining() != null &&
               header.getRateLimitReset() != null)
            {
                sessionLimiter.update(header.getRateLimitRemaining(), header.getRateLimitReset());
            }

            if(!IsResponseValid(responce))
            {
                return null;
            }

            JsonElement jelement = new JsonParser().parse(responce);
            JsonObject jobject = jelement.getAsJsonObject();

            JsonElement je = jobject.get("results");
            if(je.isJsonNull())
                return null;

            JsonArray jsonArray = je.getAsJsonArray();

            for(int i = 0; i < jsonArray.size(); ++i)
            {
                JsonElement elem = jsonArray.get(i);
                if(elem.isJsonNull())
                    continue;
                JsonObject jobj = elem.getAsJsonObject();

                elem = jobj.get("title");
                if(elem.isJsonNull())
                    continue;

                String foundVideoTitle = elem.getAsString();
                if(foundVideoTitle.equals(videoTitle))
                {
                    elem = jobj.get("id");
                    if(!elem.isJsonNull())
                    {
                        tmdbId = elem.getAsString();
                        break;
                    }
                }
            }

            response.setData(tmdbId);
            response.setResponseStatus(Response.ResponseStatus.FETCH_OK);
        }
        else
        {
            response.setCooldownTime(sessionLimiter.getCooldownTime());
            response.setResponseStatus(Response.ResponseStatus.FETCH_LIMIT_EXCEEDED);
        }

        return response;
    }

    private boolean IsResponseValid(String json)
    {
        if(json == null || json.isEmpty())
            return false;

        return true;
    }

    private enum VideoType
    {
        MOVIE,
        SERIES
    }

    private String tmdbApiKey;
    private String tmdbBaseURL = "https://api.themoviedb.org/";
    private int tmdbApiVersion;

    private List<Cell> dataToFetch = null;
    private WebJsonDld webJsonDlg;
    private WebImageDlg webImageDlg;
    private SessionLimiter sessionLimiter = new SessionLimiter(40);
}

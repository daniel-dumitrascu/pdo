package org.projects.centralpoint.middleware.Helper;

import org.projects.centralpoint.Utils.Web.Extern.Models.VideoRawJsonEntity;
import org.projects.centralpoint.middleware.Models.Video;
import org.projects.centralpoint.Utils.Web.LinkCreator;
import org.projects.centralpoint.Utils.Web.WebJsonDld;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Locale;
import java.time.format.DateTimeFormatter;

public class VideoModelHelper implements ModelHelper
{
    public void UpdateModelWithExternData(Object model, String url)
    {
        if(model != null && model instanceof Video)
        {
            Video videoModel = (Video)model;
            WebJsonDld webJson = new WebJsonDld();
            webJson.Load(url);

            String json = webJson.GetPageBody();

            Gson gson = new GsonBuilder().create();
            VideoRawJsonEntity videoRawObject = gson.fromJson(json, VideoRawJsonEntity.class);
            if(videoRawObject != null && videoRawObject.Response != null &&
               videoRawObject.Response.equals("True") && videoRawObject.Title.equals(videoModel.getTitle()))
            {
                // We don't need to set a title because the user would always provide a title
                // and there's no reason why we would want to overwrite the title once
                // we have the information from OMDB

                //videoModel.setOmdbLink(url);
                videoModel.setDescription(videoRawObject.Plot);

                String movieRuntime = videoRawObject.Runtime.replaceAll("\\D+","");
                if(!movieRuntime.isEmpty())
                {
                    try
                    {
                        videoModel.setRuntime(Integer.parseInt(movieRuntime));
                    }
                    catch (NumberFormatException nfe)
                    {
                        //TODO I cannot have a member logger because this function is static
                        //logger.error("The conversion of the file to string text was not possible.");
                    }
                }

                LocalDate releaseDate = null;

                try
                {
                    if(videoRawObject.Released != null)
                    {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMM yyyy");
                        formatter = formatter.withLocale(Locale.UK);
                        releaseDate = LocalDate.parse(videoRawObject.Released, formatter);
                    }
                }
                catch(Exception e)
                {
                    releaseDate = null;
                }

                if(videoModel.getReleaseYear() == null || videoModel.getReleaseYear().isEmpty())
                {
                    videoModel.setReleaseYear(videoRawObject.Year);
                }

                videoModel.setReleaseDate(releaseDate);
                videoModel.setImdbLink(LinkCreator.ConstructLinkForIMDB(videoRawObject.imdbID));

                try
                {
                    videoModel.setInternetScore(new BigDecimal("" + videoRawObject.imdbRating));
                }
                catch (NumberFormatException nfe)
                {
                    //TODO I cannot have a member logger because this function is static
                    //logger.error("Cannot convert IMDB string score to integer.");
                }

                // Add the genres to the video model
                /*List<String> genresArr = StringHelper.TokenizeByDelimiter(videoRawObject.Genre, ",", false);
                List<Genre> genreList = new ArrayList<Genre>();
                for(String genreStr : genresArr)
                {
                    Genre genre = new Genre();
                    genre.setGenre(genreStr);
                    genre.setVideo(videoModel);
                    genreList.add(genre);
                }

                videoModel.setGenres(genreList);

                // Add the countries to the video model
                List<String> countriesArr = StringHelper.TokenizeByDelimiter(videoRawObject.Country, ",", false);
                List<Country> countryList = new ArrayList<Country>();
                for(String countryStr : countriesArr)
                {
                    Country country = new Country();
                    country.setCountry(countryStr);
                    country.setVideo(videoModel);
                    countryList.add(country);
                }

                videoModel.addCountries(countryList);

                // Add the languages to the video model
                List<String> languagesArr = StringHelper.TokenizeByDelimiter(videoRawObject.Language, ",", false);
                List<Language> languageList = new ArrayList<Language>();
                for(String languageStr : languagesArr)
                {
                    Language language = new Language();
                    language.setLanguage(languageStr);
                    language.setVideo(videoModel);
                    languageList.add(language);
                }

                videoModel.addLanguages(languageList);

                // Add the actors to the video model
                List<String> actorsArr = StringHelper.TokenizeByDelimiter(videoRawObject.Actors, ",", false);
                List<ActorJobs> actorList = new ArrayList<ActorJobs>();
                for(String actorStr : actorsArr)
                {
                    Person person = new Person();
                    person.setName(actorStr);
                    person.setRole(Types.PeopleRole.ACTOR.getIntValue());

                    ActorJobs actor = new ActorJobs();
                    actor.setPerson(person);
                    actor.setVideo(videoModel);
                    actorList.add(actor);
                }

                videoModel.addActors(actorList);*/
            }
            else
            {
                // If we end here then there's a problem with the loading the info for this item
                // We need to write this in a separate file.
                //TODO maybe I should write this error message directly in logs
                System.out.println("Couldn't retrive the extra information for this video: " + videoModel.getTitle());
            }
        }
    }
}
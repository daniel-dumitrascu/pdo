package org.projects.centralpoint.Extern.Fetching;

import org.apache.commons.io.FilenameUtils;
import org.projects.centralpoint.Defines.Types;
import org.projects.centralpoint.Settings.AppSettings;
import org.projects.centralpoint.Utils.Thread.ThreadHelper;
import org.projects.centralpoint.middleware.Models.*;
import org.projects.centralpoint.middleware.Services.ActorService;
import org.projects.centralpoint.middleware.Services.PersonService;
import org.projects.centralpoint.middleware.Services.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.projects.centralpoint.Extern.Fetching.Response.ResponseStatus.FETCH_OK;
import static org.projects.centralpoint.Utils.DbData.DataHelper.getFormattedCountry;
import static org.projects.centralpoint.Utils.DbData.DataHelper.getFormattedGenre;

@Resource(name="videoExternFetcher")
@Component("videoExternFetcher")
public class VideoExternFetcher implements ExternFetcher
{
    public VideoExternFetcher()
    {
        videoDataFetcher = new TmdbVideoDataFetcher("3eddce8d99df267a4725c1a5e6e3eb7d", 3);
        lastTimeFetch = System.currentTimeMillis();
    }

    public void fetch()
    {
        synchronized (currTimestamp)
        {
            if ((currTimestamp != 0L && !lastTimestamp.equals(currTimestamp)) || isTimeToWork())
            {
                System.out.println("[Worker Thread] Start fetching the video data.");
                Long localCurrTimestamp = currTimestamp;

                List<Video> toUpdateVideoList = videoService.GetWaitingVideos();

                if(toUpdateVideoList != null)
                {
                    while(!toUpdateVideoList.isEmpty())
                    {
                        /*
                         * We are fetching the data for the videos with the waiting status
                         * but, in the same time, we don't forget about the videos for which
                         * the extern data haven't been fetched.
                         *
                         * We are going to fetch until the return list is empty.
                         */
                        toUpdateVideoList = fetchDataForVideos(toUpdateVideoList);
                    }

                    lastTimestamp = localCurrTimestamp;
                    lastTimeFetch = System.currentTimeMillis();
                    System.out.println("[Worker Thread] DONE");
                }
            }
        }
    }

    public void notifyForWork()
    {
        synchronized (currTimestamp)
        {
            currTimestamp = System.currentTimeMillis();
        }
    }

    private List<Video> fetchDataForVideos(List<Video> videoColl)
    {
        List<Video> collToUpdateInNextStep = new ArrayList<>();

        for(Video video : videoColl)
        {
            Response videoRes;

            if(video.getTmdbID() != null && !video.getTmdbID().isEmpty())
            {
                videoRes = videoDataFetcher.getMovie(video.getTmdbID(), VideoDataFetcher.IdType.TMDB);
            }
            else if(video.getImdbID() != null && !video.getImdbID().isEmpty())
            {
                videoRes = videoDataFetcher.getMovie(video.getImdbID(), VideoDataFetcher.IdType.IMDB);
            }
            else
            {
                videoRes = videoDataFetcher.getMovie(video.getTitle(), video.getReleaseYear());
            }

            if(videoRes == null)
                continue;

            if(videoRes.getResponseStatus() == FETCH_OK)
            {
                if(videoRes.getData() != null)
                {
                    VideoData videoData = (VideoData)videoRes.getData();
                    updateWithFetchedData(video, videoData);

                    /*
                     * If we managed to get the external data for a certain movie
                     * we are going to get the poster data as well but only if
                     * we don't have the poster already downloaded on our system
                     */
                    String posterFilePath = constrAbsPosterFilePath(video);
                    if(!posterFilePath.isEmpty())
                    {
                        File file = new File(posterFilePath);
                        if(!file.exists())
                        {
                            Response imageRes = videoDataFetcher.getPoster(videoData.getPosterPath(), VideoDataFetcher.PosterSize.BIG);
                            if(imageRes.getResponseStatus() == Response.ResponseStatus.FETCH_OK)
                            {
                                PosterData posterData = (PosterData)imageRes.getData();
                                savePosterData(video, posterData, posterFilePath);
                            }
                        }
                    }

                    video.setFetchingStatus(FetchingStatus.SUCCESS.getIntValue());
                }
            }
            else if (videoRes.getResponseStatus() == Response.ResponseStatus.FETCH_LIMIT_EXCEEDED)
            {
                collToUpdateInNextStep.add(video);
                ThreadHelper.sleepCurrentThread(videoRes.getCooldownTime() * 1000);
            }
            else
            {
                System.out.println("[Worker Thread] Fail to update movie: " + video.getTitle());
                video.setFetchingStatus(FetchingStatus.FAIL.getIntValue());
            }

            videoService.updateVideo(video);
        }

        return collToUpdateInNextStep;
    }

    private void savePosterData(Video video, PosterData posterData, String posterFilePath)
    {
        InputStream is = (InputStream)posterData.getData();

        if(is == null)
            return;

        OutputStream os = null;

        try
        {
            os = new FileOutputStream(posterFilePath);

            byte[] buffer = new byte[2048];
            int length;

            while ((length = is.read(buffer)) != -1)
            {
                os.write(buffer, 0, length);
            }
        }
        catch (FileNotFoundException e)
        {
            //TODO error msg
            e.printStackTrace();
            return;
        }
        catch (IOException e)
        {
            //TODO error msg
            e.printStackTrace();
            return;
        }
        finally
        {
            try
            {
                os.close();
            }
            catch (IOException e)
            {
                System.out.println("Finally IOException :- " + e.getMessage());
            }
        }
    }

    private void updateWithFetchedData(Video video, VideoData externData)
    {
        if(video != null && externData != null)
        {
            if(video.getTitle() == null || video.getTitle().isEmpty())
                video.setTitle(externData.getTitle());

            if(video.getTmdbID() == null || video.getTmdbID().isEmpty())
                video.setTmdbID(Integer.toString(externData.getTmdbId()));

            if(video.getImdbID() == null || video.getImdbID().isEmpty())
                video.setImdbID(externData.getImdbId());

            if(video.getDescription() == null || video.getDescription().isEmpty())
                video.setDescription(externData.getOverview());

            if(video.getReleaseDate() == null)
            {
                LocalDate releaseDate = null;
                String stringReleaseDate = externData.getReleaseDate();

                try
                {
                    if(stringReleaseDate != null)
                    {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-d");
                        formatter = formatter.withLocale(Locale.UK);
                        releaseDate = LocalDate.parse(stringReleaseDate, formatter);
                    }
                }
                catch(Exception e)
                {
                    releaseDate = null;
                }

                video.setReleaseDate(releaseDate);
            }

            if(video.getRuntime() == 0)
                video.setRuntime(externData.getRuntime());

            if(video.getInternetScore() == null || video.getInternetScore().compareTo(BigDecimal.ZERO) == 0)
                video.setInternetScore(new BigDecimal("" + externData.getVoteAverage()));

            if(video.getInternetVotesNumber() == 0)
                video.setInternetVotesNumber(externData.getVoteCount());

            video.setVideoStatus(externData.getStatus());

            if(video.getBudget() == 0)
                video.setBudget(externData.getBudget());

            if(video.getRevenue() == 0)
                video.setRevenue(externData.getRevenue());

            if(video.getTmdbPosterPath() == null || video.getTmdbPosterPath().isEmpty())
                video.setTmdbPosterPath(externData.getPosterPath());

            /*
             * Store the genres information
             */
            List<String> strGenreColl = externData.getGenres();
            Set<Genre> currGenreColl = video.getGenres();

            for(String strGenre : strGenreColl)
            {
                if(strGenre.isEmpty())
                    continue;

                String genreToStore = getFormattedGenre(strGenre);
                if(!genreToStore.isEmpty())
                {
                    Genre g = new Genre();
                    g.setGenre(genreToStore);
                    g.setVideo(video);

                    if(!currGenreColl.contains(g))
                        currGenreColl.add(g);
                }
            }

            /*
             * Store the country information
             */
            List<String> strCountryColl = externData.getCountries();
            Set<Country> currCountryColl = video.getCountries();

            for(String strCountry : strCountryColl)
            {
                if(strCountry.isEmpty())
                    continue;

                String countryToStore = getFormattedCountry(strCountry);
                if(!countryToStore.isEmpty())
                {
                    Country c = new Country();
                    c.setCountry(strCountry);
                    c.setVideo(video);

                    if(!currCountryColl.contains(c))
                        currCountryColl.add(c);
                }
            }

            /*
             * Store the language information
             */
            List<String> strLanguageColl = externData.getLanguages();
            List<Language> currLanguageColl = video.getLanguages();

            for(String strLanguage : strLanguageColl)
            {
                if(strLanguage.isEmpty())
                    continue;

                Language l = new Language();
                l.setLanguage(strLanguage);
                l.setVideo(video);

                if(!currLanguageColl.contains(l))
                    currLanguageColl.add(l);
            }

            /*
             * Store the actor information
             */
            for(PersonData pd : externData.getActors())
            {
                if(pd.getName().isEmpty())
                    continue;

                /*
                 *  If we have it, get the Person from Db
                 */
                Person p = pService.getPerson(pd.getName());
                ActorJobs actorJobs = null;
                List<ActorJobs> currActorJobsColl = video.getActors();

                if(p == null)
                {
                    /*
                     * We don't have any person in the db with this name
                     * We will create now a person and a new actorJobs
                     */
                    p = createPersonFromData(pd);
                    actorJobs = new ActorJobs();
                    actorJobs.setPerson(p);
                    actorJobs.setVideo(video);

                    /*
                     * Before we add it we must check to see if the actorJobs
                     * was already added to the current actorJobs coll
                     */
                    if(!currActorJobsColl.contains(actorJobs))
                    {
                        /*
                         * Add the actorJobs to the video
                         */
                        video.addActor(actorJobs);
                    }
                }
                else
                {
                    actorJobs = new ActorJobs();
                    actorJobs.setPerson(p);
                    actorJobs.setVideo(video);

                    /*
                     * We need to do 2 checks:
                     * 1. Check to see if we have already added the person in the coll that wasn't yet written to db
                     * 2. Search for the ActorJobs in the database as well
                     */
                    if(!currActorJobsColl.contains(actorJobs) && !aService.actorExists(actorJobs))
                    {
                        /*
                         * Add the actorJobs to the video
                         */
                        video.addActor(actorJobs);
                    }
                }
            }
        }
    }

    private Person createPersonFromData(PersonData pd)
    {
        Person p = new Person();
        p.setName(pd.getName());
        p.setBiography(pd.getBiography());
        p.setBirthday(pd.getBirthday());
        p.setDeathday(pd.getDeathday());
        if(pd.getGender() != null)
            p.setGender(pd.getGender().getIntValue());
        p.setTmdbId(pd.getId());
        p.setRole(Types.PeopleRole.ACTOR.getIntValue());

        return p;
    }

    private String constrAbsPosterFilePath(Video video)
    {
        /*
         * Compute the file path where this needs to be saved
         */
        String filePath = "";
        if(video.getTmdbPosterPath() != null && !video.getTmdbPosterPath().isEmpty())
        {
            filePath = appSettings.getDbResourcesPath();
            filePath += "movies/";
            filePath += "posters/";
            String newFilename = String.valueOf(video.getId()) + "_poster";
            filePath += newFilename;
            filePath += "." + FilenameUtils.getExtension(video.getTmdbPosterPath());
        }

        return filePath;
    }

    private boolean isTimeToWork()
    {
        Long currTime = System.currentTimeMillis();
        return ((currTime - lastTimeFetch) / 1000) > timeBetweenJobs;
    }

    @Autowired
    private VideoService videoService;

    @Autowired
    private PersonService pService;

    @Autowired
    private ActorService aService;

    @Autowired
    private AppSettings appSettings;

    private VideoDataFetcher videoDataFetcher;

    private Long currTimestamp = 0L;
    private Long lastTimestamp = 0L;
    private Long lastTimeFetch = 0L;
    private int timeBetweenJobs = 10800; // 3 hours in seconds
}

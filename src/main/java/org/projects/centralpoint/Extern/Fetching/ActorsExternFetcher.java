package org.projects.centralpoint.Extern.Fetching;

import org.apache.commons.io.FilenameUtils;
import org.projects.centralpoint.Settings.AppSettings;
import org.projects.centralpoint.Utils.Thread.ThreadHelper;
import org.projects.centralpoint.middleware.Models.Person;
import org.projects.centralpoint.middleware.Models.Video;
import org.projects.centralpoint.middleware.Services.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static org.projects.centralpoint.Extern.Fetching.Response.ResponseStatus.FETCH_OK;


@Resource(name="peopleExternFetcher")
@Component("peopleExternFetcher")
public class ActorsExternFetcher implements ExternFetcher
{
    public ActorsExternFetcher()
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
                System.out.println("[Worker Thread] Start fetching the actor data.");
                Long localCurrTimestamp = currTimestamp;

                List<Person> waitingPersonList = pService.getWaitingPersons();

                if(waitingPersonList != null)
                {
                    while(!waitingPersonList.isEmpty())
                    {
                        /*
                         * We are fetching the data for the videos with the waiting status
                         * but, in the same time, we don't forget about the videos for which
                         * the extern data haven't been fetched.
                         *
                         * We are going to fetch until the return list is empty.
                         */
                        waitingPersonList = fetchDataForPeople(waitingPersonList);
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

    private List<Person> fetchDataForPeople(List<Person> peopleColl)
    {
        List<Person> collToUpdateInNextStep = new ArrayList<>();

        for(Person person : peopleColl)
        {
            Response personRes;

            if(person.getTmdbId() != 0)
                personRes = videoDataFetcher.getPerson(String.valueOf(person.getTmdbId()), VideoDataFetcher.IdType.TMDB);
            else
                personRes = videoDataFetcher.getPerson(person.getName());

            if(personRes == null)
                continue;

            if(personRes.getResponseStatus() == FETCH_OK)
            {
                if(personRes.getData() != null)
                {
                    PersonData personData = (PersonData)personRes.getData();
                    updateWithFetchedData(person, personData);

                    /*
                     * If we managed to get the external data for a certain movie
                     * we are going to get the poster data as well but only if
                     * we don't have the poster already downloaded on our system
                     */
                    String posterFilePath = constrAbsPosterFilePath(person);
                    if(!posterFilePath.isEmpty())
                    {
                        File file = new File(posterFilePath);
                        if(!file.exists())
                        {
                            Response imageRes = videoDataFetcher.getPoster(personData.getImagePath(), VideoDataFetcher.PosterSize.BIG);
                            if(imageRes.getResponseStatus() == FETCH_OK)
                            {
                                PosterData posterData = (PosterData)imageRes.getData();
                                savePosterData(person, posterData, posterFilePath);
                            }
                        }
                    }

                    person.setFetchingStatus(FetchingStatus.SUCCESS.getIntValue());
                }
            }
            else if (personRes.getResponseStatus() == Response.ResponseStatus.FETCH_LIMIT_EXCEEDED)
            {
                collToUpdateInNextStep.add(person);
                ThreadHelper.sleepCurrentThread(personRes.getCooldownTime() * 1000);
            }
            else
            {
                System.out.println("[Worker Thread] Fail to update person: " + person.getName());
                person.setFetchingStatus(FetchingStatus.FAIL.getIntValue());
            }

            pService.updatePerson(person);
        }

        return collToUpdateInNextStep;
    }

    private String constrAbsPosterFilePath(Person person)
    {
        /*
         * Compute the file path where this needs to be saved
         */
        String filePath = "";
        if(person.getTmdbPosterPath() != null && !person.getTmdbPosterPath().isEmpty())
        {
            filePath = appSettings.getDbResourcesPath();
            filePath += "actors/posters/";
            String newFilename = String.valueOf(person.getId()) + "_poster";
            filePath += newFilename;
            filePath += "." + FilenameUtils.getExtension(person.getTmdbPosterPath());
        }

        return filePath;
    }

    private void savePosterData(Person person, PosterData posterData, String posterFilePath)
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
            //TODO error message
            e.printStackTrace();
            return;
        }
        catch (IOException e)
        {
            //TODO error message
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

    private void updateWithFetchedData(Person person, PersonData personData)
    {
        if(person != null && personData != null)
        {
            if(person.getName() == null || person.getName().isEmpty())
                person.setName(personData.getName());

            if(person.getTmdbId() == 0)
                person.setTmdbId(personData.getId());

            if(person.getBiography() == null || person.getBiography().isEmpty())
                person.setBiography(personData.getBiography());

            if(person.getBirthday() == null)
                person.setBirthday(personData.getBirthday());

            if(person.getDeathday() == null)
                person.setDeathday(personData.getDeathday());

            person.setGender(personData.getGender().getIntValue());

            if(person.getTmdbPosterPath() == null || person.getTmdbPosterPath().isEmpty())
                person.setTmdbPosterPath(personData.getImagePath());

            if(person.getBirthplace() == null || person.getBirthplace().isEmpty())
                person.setBirthplace(personData.getPlaceOfBirth());
        }
    }

    private boolean isTimeToWork()
    {
        Long currTime = System.currentTimeMillis();
        return ((currTime - lastTimeFetch) / 1000) > timeBetweenJobs;
    }

    @Autowired
    private PersonService pService;

    @Autowired
    private AppSettings appSettings;

    private Long currTimestamp = 0L;
    private Long lastTimestamp = 0L;
    private Long lastTimeFetch = 0L;
    private VideoDataFetcher videoDataFetcher;
    private int timeBetweenJobs = 10800; // 3 hours in seconds
}

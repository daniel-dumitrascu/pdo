package org.projects.centralpoint.Database;

import org.projects.centralpoint.Filter.SearchFilter;
import org.projects.centralpoint.Utils.Date.DateHelper;
import org.projects.centralpoint.middleware.Models.Country;
import org.projects.centralpoint.middleware.Models.Genre;
import org.projects.centralpoint.middleware.Models.Video;
import org.hibernate.Session;
import org.projects.centralpoint.middleware.Models.Video_;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.*;

public class VideoFilter
{
    public VideoFilter(SearchFilter searchFilter, DbSchema schema)
    {
        loadedSearchFilter = searchFilter;
        indexLocator = new TreeMap<>();
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("application.properties");
        Properties props = new Properties();

        try
        {
            props.load(inputStream);
            inputStream.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }

        JDBC_DRIVER = props.getProperty("jdbc.driverClassName");
        if(schema == DbSchema.MAIN)
            DB_URL = props.getProperty("jdbc.main.url");
        else if(schema == DbSchema.TEST)
            DB_URL = props.getProperty("jdbc.test.url");
        USER = props.getProperty("jdbc.username");
        PASS = props.getProperty("jdbc.password");
    }

    /*****************************************************************************************/
    /*****************************************************************************************/

    public boolean IsFilterSet() { return (loadedSearchFilter != null); }

    /*****************************************************************************************/
    /*****************************************************************************************/

    public void UpdateFilter(SearchFilter filter) { loadedSearchFilter = filter; }

    /*****************************************************************************************/
    /*****************************************************************************************/

    public List<Video> ApplyFilter(Session session)
    {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Video> criteriaQuery = null;
        List<Video> results = null;

        if(loadedSearchFilter != null)
        {
            List<Video> firstPassResults = null;
            criteriaQuery = builder.createQuery(Video.class);
            Root<Video> videoRoot = criteriaQuery.from(Video.class);
            List<Predicate> predicates = new ArrayList<Predicate>();
            Set<Genre> genresToSearch = new HashSet<>();
            Set<Country> countriesToSearch = new HashSet<>();

            if(loadedSearchFilter.GetFilterCount() > 0)
            {
                /*
                 * First pass - create query and get the data from db
                 */
                while(loadedSearchFilter.GetNextFilter())
                {
                    //TODO https://stackoverflow.com/questions/12199433/jpa-criteria-api-with-multiple-parameters
                    switch (loadedSearchFilter.GetCurrentFilterType())
                    {
                        case FILTER_NAME:
                        {
                            if (loadedSearchFilter.IsFilterSettingsON(SearchFilter.FilterSettings.FILTER_EXACT_TITLE))
                                predicates.add(builder.equal(videoRoot.get("title"), loadedSearchFilter.GetCurrentFilterValue()));
                            else
                                predicates.add(builder.like(videoRoot.get("title"), "%" + loadedSearchFilter.GetCurrentFilterValue()+ "%"));

                            break;
                        }
                        case FILTER_SAW_IT:
                        {
                            predicates.add(builder.equal(videoRoot.get("sawIt"), (Boolean)loadedSearchFilter.GetCurrentFilterValue()));
                            break;
                        }
                        case FILTER_YEAR:
                        {
                            Object filterVal = loadedSearchFilter.GetCurrentFilterValue();

                            if(loadedSearchFilter.IsFilterSettingsON(SearchFilter.FilterSettings.FILTER_BEFORE_YEAR))
                                predicates.add(builder.lessThan(videoRoot.get("releaseYear"), (String)filterVal));
                            else if(loadedSearchFilter.IsFilterSettingsON(SearchFilter.FilterSettings.FILTER_AFTER_YEAR))
                                predicates.add(builder.greaterThan(videoRoot.get("releaseYear"), (String)filterVal));
                            else
                                predicates.add(builder.equal(videoRoot.get("releaseYear"), (String)filterVal));

                            break;
                        }
                        case FILTER_PERSONAL_SCORE:
                        {
                            Object filterVal = loadedSearchFilter.GetCurrentFilterValue();

                            if(loadedSearchFilter.IsFilterSettingsON(SearchFilter.FilterSettings.FILTER_WITHIN_PERSONAL_SCORE))
                                predicates.add(builder.lessThan(videoRoot.get("personalScore"), (BigDecimal)filterVal));
                            else if(loadedSearchFilter.IsFilterSettingsON(SearchFilter.FilterSettings.FILTER_EXCEEDS_PERSONAL_SCORE))
                                predicates.add(builder.greaterThan(videoRoot.get("personalScore"), (BigDecimal)filterVal));
                            else
                                predicates.add(builder.equal(videoRoot.get("personalScore"), (BigDecimal)filterVal));

                            break;
                        }
                        case FILTER_IMDB_SCORE:
                        {
                            Object filterVal = loadedSearchFilter.GetCurrentFilterValue();

                            if(loadedSearchFilter.IsFilterSettingsON(SearchFilter.FilterSettings.FILTER_WITHIN_IMDB_SCORE))
                                predicates.add(builder.lessThan(videoRoot.get("internetScore"), (BigDecimal)filterVal));
                            else if(loadedSearchFilter.IsFilterSettingsON(SearchFilter.FilterSettings.FILTER_EXCEEDS_IMDB_SCORE))
                                predicates.add(builder.greaterThan(videoRoot.get("internetScore"), (BigDecimal)filterVal));
                            else
                                predicates.add(builder.equal(videoRoot.get("internetScore"), (BigDecimal)filterVal));

                            break;
                        }
                        case FILTER_RUNTIME:
                        {
                            Object filterVal = loadedSearchFilter.GetCurrentFilterValue();

                            if(loadedSearchFilter.IsFilterSettingsON(SearchFilter.FilterSettings.FILTER_WITHIN_RUNTIME))
                                predicates.add(builder.lessThan(videoRoot.get("runtime"), (Integer)filterVal));
                            else if(loadedSearchFilter.IsFilterSettingsON(SearchFilter.FilterSettings.FILTER_EXCEEDS_RUNTIME))
                                predicates.add(builder.greaterThan(videoRoot.get("runtime"), (Integer)filterVal));
                            else
                                predicates.add(builder.equal(videoRoot.get("runtime"), (Integer)filterVal));

                            break;
                        }
                        case FILTER_STORED_LOCATION:
                        {
                            Object filterVal = loadedSearchFilter.GetCurrentFilterValue();
                            predicates.add(builder.equal(videoRoot.get("storageName"), (String)filterVal));
                            break;
                        }
                        case FILTER_GENRE:
                        {
                            ArrayList<String> videoGenres = (ArrayList<String>)loadedSearchFilter.GetCurrentFilterValue();
                            for(String g : videoGenres)
                            {
                                Genre genre = new Genre();
                                genre.setGenre(g);
                                genresToSearch.add(genre);
                            }
                            break;
                        }
                        case FILTER_COUNTRY:
                        {
                            ArrayList<String> videoCountries = (ArrayList<String>)loadedSearchFilter.GetCurrentFilterValue();
                            for(String c : videoCountries)
                            {
                                Country country = new Country();
                                country.setCountry(c);
                                countriesToSearch.add(country);
                            }
                            break;
                        }
                    }
                }

                /* Get only the videos that match the searched genres */
                if(!genresToSearch.isEmpty())
                {
                    SetJoin<Video, Genre> joinOnGenre = videoRoot.join(Video_.genres, JoinType.INNER);
                    joinOnGenre.on(builder.and(builder.equal(joinOnGenre.get("genre"), genresToSearch.iterator().next().getGenre())));
                }

                /* Get only the videos that match the searched countries */
                if(!countriesToSearch.isEmpty())
                {
                    SetJoin<Video, Country> joinOnCountry = videoRoot.join(Video_.countries, JoinType.INNER);
                    joinOnCountry.on(builder.and(builder.equal(joinOnCountry.get("country"), countriesToSearch.iterator().next().getCountry())));
                }
            }

            criteriaQuery.select(videoRoot).where(predicates.toArray(new Predicate[]{}));
            results = session.createQuery(criteriaQuery).getResultList();

            /*
             * Second pass - do extra manipulation on the fetched data
             */
            if(genresToSearch.size() > 1)
            {
                //TODO continue - refactor this big code into something more small, a general function
                //filterVideoBy(results, )
                List<Video> secondPassResults = new ArrayList<>();

                /*
                 * It's time now to filter here the extra genres
                 */
                for(Video v : results)
                {
                    Set<Genre> videoGenres = v.getGenres();
                    if(videoGenres.containsAll(genresToSearch))
                        secondPassResults.add(v);
                }

                results = secondPassResults;
            }

            if(countriesToSearch.size() > 1)
            {
                List<Video> secondPassResults = new ArrayList<>();

                /*
                 * It's time now to filter here the extra genres
                 */
                for(Video v : results)
                {
                    Set<Country> videoCountries = v.getCountries();
                    if(videoCountries.containsAll(countriesToSearch))
                        secondPassResults.add(v);
                }

                results = secondPassResults;
            }

            /*select (subquery1.video_key) from
                (
                        select mspublic.video.video_key from mspublic.video
                        inner join mspublic.video_genres on (mspublic.video_genres.video_key = mspublic.video.video_key)
                        where mspublic.video_genres.genre = 'drama'
                ) subquery1
            inner join mspublic.video_genres vg on (vg.video_key = subquery1.video_key)
            where vg.genre = 'animation';*/
        }

        return results;
    }

    /*****************************************************************************************/
    /*****************************************************************************************/

    public Object GetValueForFilter(SearchFilter.FilterType filter)
    {
        Object userInputVal = loadedSearchFilter.GetFilterValue(filter);

        if(filter == SearchFilter.FilterType.FILTER_NAME &&
           !loadedSearchFilter.IsFilterSettingsON(SearchFilter.FilterSettings.FILTER_EXACT_TITLE))
        {
            userInputVal = "%" + userInputVal + "%";
        }
        else if(filter == SearchFilter.FilterType.FILTER_YEAR)
        {
            userInputVal = DateHelper.ConvertStringToSqlDate((String)userInputVal);
        }

        return userInputVal;
    }

    private SearchFilter loadedSearchFilter = null;
    private TreeMap<SearchFilter.FilterType, Integer> indexLocator;

    String JDBC_DRIVER;
    String DB_URL;
    String USER;
    String PASS;
}


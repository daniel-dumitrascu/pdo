package org.centralpoint.Database;

import org.hibernate.SessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.projects.centralpoint.Database.DbSchema;
import org.projects.centralpoint.Database.VideoFilter;
import org.projects.centralpoint.Defines.Types;
import org.projects.centralpoint.Filter.SearchFilter;
import org.projects.centralpoint.middleware.Models.Country;
import org.projects.centralpoint.middleware.Models.Genre;
import org.projects.centralpoint.middleware.Models.Video;
import org.projects.centralpoint.middleware.Services.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;


//@TransactionConfiguration(defaultRollback=false) // This is used to persist the data in db after running the test
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:test-config.xml")
@WebAppConfiguration
@Transactional
public class VideoFilterTest
{
    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private VideoService service;

    @Test
    public void applyFilterTitleTest()
    {
        //TODO what happens when we don't provide a title?
        //TODO what happens when we don't tick "exactly main title"? What is the default?

        // The title filtering (exact title)
        //======================================================================================
        Video v1 = new Video();
        Video v2 = new Video();
        Video v3 = new Video();

        v1.setTitle("The movie");
        v2.setTitle("movie The");
        v3.setTitle("The");
        v2.setVideoType("doc");
        v3.setVideoType("movie");

        service.SaveMovie(v1);
        service.SaveMovie(v2);
        service.SaveMovie(v3);

        // Construct the filter
        SearchFilter searchFilter = new SearchFilter(Types.EntityType.VIDEO);
        searchFilter.AddFilter(SearchFilter.FilterType.FILTER_NAME, "The");
        searchFilter.TurnFilterSettingsON(SearchFilter.FilterSettings.FILTER_EXACT_TITLE);

        VideoFilter qCreator = new VideoFilter(searchFilter, DbSchema.TEST);
        List<Video> result = qCreator.ApplyFilter(sessionFactory.getCurrentSession());

        // Analyze the result
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("The", result.get(0).getTitle());

        // The title filtering (non-exact option)
        //======================================================================================

        searchFilter.TurnFilterSettingsOFF(SearchFilter.FilterSettings.FILTER_EXACT_TITLE);
        result = qCreator.ApplyFilter(sessionFactory.getCurrentSession());

        // Analyze the result
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(true, result.stream().filter(o -> o.getTitle().equals("The movie")).findAny().isPresent());
        assertEquals(true, result.stream().filter(o -> o.getTitle().equals("movie The")).findAny().isPresent());
        assertEquals(true, result.stream().filter(o -> o.getTitle().equals("The")).findAny().isPresent());

        // The title filtering (only those seen by me)
        //======================================================================================
        v3.setSawIt(true);
        service.updateVideo(v3);

        searchFilter.AddFilter(SearchFilter.FilterType.FILTER_SAW_IT, true);

        result = qCreator.ApplyFilter(sessionFactory.getCurrentSession());

        // Analyze the result
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("The", result.get(0).getTitle());
    }

    @Test
    public void applyFilterReleaseYearTest()
    {
        //TODO how do I handle a negative value of the year? I should warn the user that the year is not correct
        //TODO because we have a string value we have to check that the year is a correct number and does not contain non-numeric characters, etc.
        //TODO what happens if we set "before or after" without a year? Normally, I should receive a warning that the year will not be taken into account

        // The release year filtering (default)
        //======================================================================================
        Video v1 = new Video();
        Video v2 = new Video();
        Video v3 = new Video();

        v1.setTitle("Movie 1");
        v2.setTitle("Movie 2");
        v3.setTitle("Movie 3");
        v1.setReleaseYear("1990");
        v2.setReleaseYear("2000");
        v3.setReleaseYear("3000");

        service.SaveMovie(v1);
        service.SaveMovie(v2);
        service.SaveMovie(v3);

        // Construct the filter
        SearchFilter searchFilter = new SearchFilter(Types.EntityType.VIDEO);
        searchFilter.AddFilter(SearchFilter.FilterType.FILTER_YEAR, "1990");

        VideoFilter qCreator = new VideoFilter(searchFilter, DbSchema.TEST);
        List<Video> result = qCreator.ApplyFilter(sessionFactory.getCurrentSession());

        // Analyze the result
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("1990", result.get(0).getReleaseYear());

        // The release year filtering (before this year)
        //======================================================================================
        Video v4 = new Video();

        v4.setTitle("Movie 4");
        v4.setReleaseYear("1990");

        service.SaveMovie(v4);

        // Construct the filter
        searchFilter = new SearchFilter(Types.EntityType.VIDEO);
        searchFilter.AddFilter(SearchFilter.FilterType.FILTER_YEAR, "1990");
        searchFilter.TurnFilterSettingsON(SearchFilter.FilterSettings.FILTER_BEFORE_YEAR);

        qCreator = new VideoFilter(searchFilter, DbSchema.TEST);
        result = qCreator.ApplyFilter(sessionFactory.getCurrentSession());

        // Analyze the result
        assertNotNull(result);
        assertEquals(0, result.size());

        // Reconstruct the filter
        searchFilter.RemoveFilter(SearchFilter.FilterType.FILTER_YEAR);
        searchFilter.AddFilter(SearchFilter.FilterType.FILTER_YEAR, "2001");

        qCreator = new VideoFilter(searchFilter, DbSchema.TEST);
        result = qCreator.ApplyFilter(sessionFactory.getCurrentSession());

        // Analyze the result
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(2, result.stream().filter(o -> o.getReleaseYear().equals("1990")).count());
        assertEquals(true, result.stream().filter(o -> o.getReleaseYear().equals("2000")).findAny().isPresent());

        // The release year filtering (after this year)
        //======================================================================================
        // Reconstruct the filter
        searchFilter = new SearchFilter(Types.EntityType.VIDEO);
        searchFilter.AddFilter(SearchFilter.FilterType.FILTER_YEAR, "2000");
        searchFilter.TurnFilterSettingsON(SearchFilter.FilterSettings.FILTER_AFTER_YEAR);

        qCreator = new VideoFilter(searchFilter, DbSchema.TEST);
        result = qCreator.ApplyFilter(sessionFactory.getCurrentSession());

        // Analyze the result
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("3000", result.get(0).getReleaseYear());
    }

    @Test
    public void applyFilterRuntimeTest()
    {
        //TODO how do I handle a negative runtime value? I should warn the user that the tuntime is incorrect
        //TODO what happens if we set "within or exceeding" without having a runtime? Normally,
        // I should receive a warning that the runtime will not be taken into account

        // The runtime filtering (default)
        //======================================================================================
        Video v1 = new Video();
        Video v2 = new Video();
        Video v3 = new Video();

        v1.setTitle("Movie 1");
        v2.setTitle("Movie 2");
        v3.setTitle("Movie 3");
        v1.setRuntime(100);
        v2.setRuntime(150);
        v3.setRuntime(200);

        service.SaveMovie(v1);
        service.SaveMovie(v2);
        service.SaveMovie(v3);

        // Construct the filter
        SearchFilter searchFilter = new SearchFilter(Types.EntityType.VIDEO);
        searchFilter.AddFilter(SearchFilter.FilterType.FILTER_RUNTIME, 150);

        VideoFilter qCreator = new VideoFilter(searchFilter, DbSchema.TEST);
        List<Video> result = qCreator.ApplyFilter(sessionFactory.getCurrentSession());

        // Analyze the result
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(150, result.get(0).getRuntime());

        // The runtime filtering (within runtime)
        //======================================================================================
        // Construct the filter
        searchFilter = new SearchFilter(Types.EntityType.VIDEO);
        searchFilter.AddFilter(SearchFilter.FilterType.FILTER_RUNTIME, 150);
        searchFilter.TurnFilterSettingsON(SearchFilter.FilterSettings.FILTER_WITHIN_RUNTIME);

        qCreator = new VideoFilter(searchFilter, DbSchema.TEST);
        result = qCreator.ApplyFilter(sessionFactory.getCurrentSession());

        // Analyze the result
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(true, result.stream().filter(o -> o.getRuntime() == 100).findAny().isPresent());

        // The runtime filtering (exceeding runtime)
        //======================================================================================
        // Construct the filter
        searchFilter = new SearchFilter(Types.EntityType.VIDEO);
        searchFilter.AddFilter(SearchFilter.FilterType.FILTER_RUNTIME, 150);
        searchFilter.TurnFilterSettingsON(SearchFilter.FilterSettings.FILTER_EXCEEDS_RUNTIME);

        qCreator = new VideoFilter(searchFilter, DbSchema.TEST);
        result = qCreator.ApplyFilter(sessionFactory.getCurrentSession());

        // Analyze the result
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(true, result.stream().filter(o -> o.getRuntime() == 200).findAny().isPresent());
    }

    @Test
    public void applyFilterInternetScoreTest()
    {
        //D12 cum fac handle la o valoare negativa a internet score-ului? ar trebuii sa-l atentionez pe user ca internet score-ul nu-i corect
        //D12 ce se intampla daca setam "within sau exceeding" fara avea un internet score? Normal, ar trebuii sa primesc un warning ca internet score-ul nu va fi luat in seama
        //D12 ce se intampla daca internet score-ul e mai mare decat 10? Warning catre user ca nu vom lua in seama internet score-ul acesta fiind gresit

        // The internet score filtering (default)
        //======================================================================================
        Video v1 = new Video();
        Video v2 = new Video();
        Video v3 = new Video();

        v1.setTitle("Movie 1");
        v2.setTitle("Movie 2");
        v3.setTitle("Movie 3");
        v1.setInternetScore(new BigDecimal("10"));
        v2.setInternetScore(new BigDecimal("5.8"));
        v3.setInternetScore(new BigDecimal("0"));

        service.SaveMovie(v1);
        service.SaveMovie(v2);
        service.SaveMovie(v3);

        // Construct the filter
        SearchFilter searchFilter = new SearchFilter(Types.EntityType.VIDEO);
        searchFilter.AddFilter(SearchFilter.FilterType.FILTER_IMDB_SCORE, new BigDecimal("5.8"));

        VideoFilter qCreator = new VideoFilter(searchFilter, DbSchema.TEST);
        List<Video> result = qCreator.ApplyFilter(sessionFactory.getCurrentSession());

        // Analyze the result
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(new BigDecimal("5.8"), result.get(0).getInternetScore());

        // The internet score filtering (within score)
        //======================================================================================
        // Construct the filter
        searchFilter = new SearchFilter(Types.EntityType.VIDEO);
        searchFilter.AddFilter(SearchFilter.FilterType.FILTER_IMDB_SCORE, new BigDecimal("5.8"));
        searchFilter.TurnFilterSettingsON(SearchFilter.FilterSettings.FILTER_WITHIN_IMDB_SCORE);

        qCreator = new VideoFilter(searchFilter, DbSchema.TEST);
        result = qCreator.ApplyFilter(sessionFactory.getCurrentSession());

        // Analyze the result
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(new BigDecimal("0"), result.get(0).getInternetScore());

        // The internet score filtering (exceeding runtime)
        //======================================================================================
        // Construct the filter
        searchFilter = new SearchFilter(Types.EntityType.VIDEO);
        searchFilter.AddFilter(SearchFilter.FilterType.FILTER_IMDB_SCORE, new BigDecimal("5.8"));
        searchFilter.TurnFilterSettingsON(SearchFilter.FilterSettings.FILTER_EXCEEDS_IMDB_SCORE);

        qCreator = new VideoFilter(searchFilter, DbSchema.TEST);
        result = qCreator.ApplyFilter(sessionFactory.getCurrentSession());

        // Analyze the result
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(new BigDecimal("10"), result.get(0).getInternetScore());
    }

    @Test
    public void applyFilterGenreTest()
    {
        // The internet score filtering (one genre)
        //======================================================================================
        Video v1 = new Video();
        Video v2 = new Video();
        Video v3 = new Video();

        v1.setTitle("Movie 1");
        v2.setTitle("Movie 2");
        v3.setTitle("Movie 3");

        Set<Genre> genres = new HashSet<Genre>();
        Genre g1 = new Genre();
        g1.setVideo(v1);
        g1.setGenre("action");
        genres.add(g1);
        Genre g2 = new Genre();
        g2.setVideo(v1);
        g2.setGenre("horror");
        genres.add(g2);
        v1.setGenres(genres);

        genres = new HashSet<Genre>();
        g1 = new Genre();
        g1.setVideo(v2);
        g1.setGenre("sf");
        genres.add(g1);
        v2.setGenres(genres);

        genres = new HashSet<Genre>();
        g1 = new Genre();
        g1.setVideo(v3);
        g1.setGenre("sf");
        genres.add(g1);
        g2 = new Genre();
        g2.setVideo(v1);
        g2.setGenre("horror");
        genres.add(g2);
        v3.setGenres(genres);

        service.SaveMovie(v1);
        service.SaveMovie(v2);
        service.SaveMovie(v3);

        // Construct the filter
        SearchFilter searchFilter = new SearchFilter(Types.EntityType.VIDEO);
        ArrayList<String> videoGenres = new ArrayList<>();
        videoGenres.add("sf");
        searchFilter.AddFilter(SearchFilter.FilterType.FILTER_GENRE, videoGenres);

        VideoFilter qCreator = new VideoFilter(searchFilter, DbSchema.TEST);
        List<Video> result = qCreator.ApplyFilter(sessionFactory.getCurrentSession());

        // Analyze the result
        assertNotNull(result);
        Genre setGenreAgainst = new Genre();
        setGenreAgainst.setGenre("sf");
        for(Video vid : result)
        {
            assertEquals(true, vid.getGenres().stream().filter(genre -> genre.getGenre().equals("sf") == true).findAny().isPresent());
        }

        // The internet score filtering (multiple genres)
        //======================================================================================
        // Construct the filter
        searchFilter = new SearchFilter(Types.EntityType.VIDEO);
        videoGenres = new ArrayList<>();
        videoGenres.add("sf");
        videoGenres.add("action");
        searchFilter.AddFilter(SearchFilter.FilterType.FILTER_GENRE, videoGenres);

        qCreator = new VideoFilter(searchFilter, DbSchema.TEST);
        result = qCreator.ApplyFilter(sessionFactory.getCurrentSession());

        // Analyze the result
        assertNotNull(result);
        assertEquals(0, result.size());

        // Add an extra video
        Video v4 = new Video();
        v4.setTitle("Movie 4");
        genres = new HashSet<Genre>();
        g1 = new Genre();
        g1.setVideo(v4);
        g1.setGenre("sf");
        genres.add(g1);
        g2 = new Genre();
        g2.setVideo(v4);
        g2.setGenre("action");
        genres.add(g2);
        v4.setGenres(genres);

        service.SaveMovie(v4);

        // Reconstruct the filter
        searchFilter = new SearchFilter(Types.EntityType.VIDEO);
        videoGenres = new ArrayList<>();
        videoGenres.add("sf");
        videoGenres.add("action");
        searchFilter.AddFilter(SearchFilter.FilterType.FILTER_GENRE, videoGenres);

        qCreator = new VideoFilter(searchFilter, DbSchema.TEST);
        result = qCreator.ApplyFilter(sessionFactory.getCurrentSession());

        // Analyze the result
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(true, result.get(0).getGenres().stream().filter(genre -> genre.getGenre().equals("sf") == true).findAny().isPresent());
        assertEquals(true, result.get(0).getGenres().stream().filter(genre -> genre.getGenre().equals("action") == true).findAny().isPresent());
    }

    @Test
    public void applyFilterCountryTest()
    {
        // The country filtering (one country)
        //======================================================================================
        Video v1 = new Video();
        Video v2 = new Video();
        Video v3 = new Video();

        v1.setTitle("Movie 1");
        v2.setTitle("Movie 2");
        v3.setTitle("Movie 3");

        Set<Country> countries = new HashSet<>();
        Country c1 = new Country();
        c1.setVideo(v1);
        c1.setCountry("Romania");
        countries.add(c1);
        Country c2 = new Country();
        c2.setVideo(v1);
        c2.setCountry("Franta");
        countries.add(c2);
        v1.setCountries(countries);

        countries = new HashSet<>();
        c1 = new Country();
        c1.setVideo(v2);
        c1.setCountry("Olanda");
        countries.add(c1);
        v2.setCountries(countries);

        countries = new HashSet<>();
        c1 = new Country();
        c1.setVideo(v3);
        c1.setCountry("Olanda");
        countries.add(c1);
        c2 = new Country();
        c2.setVideo(v1);
        c2.setCountry("Franta");
        countries.add(c2);
        v3.setCountries(countries);

        service.SaveMovie(v1);
        service.SaveMovie(v2);
        service.SaveMovie(v3);

        // Construct the filter
        SearchFilter searchFilter = new SearchFilter(Types.EntityType.VIDEO);

        ArrayList<String> videoCountries = new ArrayList<>();
        videoCountries.add("Olanda");
        searchFilter.AddFilter(SearchFilter.FilterType.FILTER_COUNTRY, videoCountries);

        VideoFilter qCreator = new VideoFilter(searchFilter, DbSchema.TEST);
        List<Video> result = qCreator.ApplyFilter(sessionFactory.getCurrentSession());

        // Analyze the result
        assertNotNull(result);
        Country setCountryAgainst = new Country();
        setCountryAgainst.setCountry("Olanda");
        for(Video vid : result)
        {
            assertEquals(true, vid.getCountries().stream().filter(country -> country.getCountry().equals("Olanda") == true).findAny().isPresent());
        }

        // The country filtering (multiple countries)
        //======================================================================================
        // Construct the filter
        searchFilter = new SearchFilter(Types.EntityType.VIDEO);
        videoCountries = new ArrayList<>();
        videoCountries.add("Olanda");
        videoCountries.add("Romania");
        searchFilter.AddFilter(SearchFilter.FilterType.FILTER_COUNTRY, videoCountries);

        qCreator = new VideoFilter(searchFilter, DbSchema.TEST);
        result = qCreator.ApplyFilter(sessionFactory.getCurrentSession());

        // Analyze the result
        assertNotNull(result);
        assertEquals(0, result.size());

        // Add an extra video
        Video v4 = new Video();
        v4.setTitle("Movie 4");
        countries = new HashSet<>();
        c1 = new Country();
        c1.setVideo(v4);
        c1.setCountry("Olanda");
        countries.add(c1);
        c2 = new Country();
        c2.setVideo(v4);
        c2.setCountry("Romania");
        countries.add(c2);
        v4.setCountries(countries);

        service.SaveMovie(v4);

        // Reconstruct the filter
        searchFilter = new SearchFilter(Types.EntityType.VIDEO);
        videoCountries = new ArrayList<>();
        videoCountries.add("Olanda");
        videoCountries.add("Romania");
        searchFilter.AddFilter(SearchFilter.FilterType.FILTER_COUNTRY, videoCountries);

        qCreator = new VideoFilter(searchFilter, DbSchema.TEST);
        result = qCreator.ApplyFilter(sessionFactory.getCurrentSession());

        // Analyze the result
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(true, result.get(0).getCountries().stream().filter(country -> country.getCountry().equals("Olanda") == true).findAny().isPresent());
        assertEquals(true, result.get(0).getCountries().stream().filter(country -> country.getCountry().equals("Romania") == true).findAny().isPresent());
    }
}

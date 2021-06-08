package org.projects.centralpoint.middleware.Models;

import java.math.BigDecimal;
import java.util.*;
import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "video")
public class Video
{
    public Video() {
        // Setting the default data
        this.setVideoType("unspecified");
        this.setAddedToDbDate(LocalDate.now()); //TODO this date must be written by the postgresql server and not by me
    }

    @Id
    @Column(name = "video_key")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToMany(mappedBy="video", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("genre ASC")
    private Set<Genre> genres;

    @OneToMany(mappedBy="video", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Country> countries;

    @OneToMany(mappedBy="video", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Language> languages;

    @OneToMany(mappedBy="video", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ActorJobs> actorJobs;

    @Column(name = "title", nullable = false, unique = true)
    private String title;

    @Column(name = "also_known_as")
    private String secondTitle;

    @Column(name = "description")
    private String description;

    @Column(name = "video_type")
    private String videoType;

    @Column(name = "video_status")
    private String videoStatus;

    @Column(name = "budget")
    private int budget;

    @Column(name = "revenue")
    private int revenue;

    @Column(name = "saw_it")
    private boolean sawIt;

    @Column(name = "quality")
    private String quality = "unspecified";

    @Column(name = "personal_score")
    private BigDecimal personalScore;

    @Column(name = "release_year")
    private String releaseYear;

    @Column(name = "release_date")
    private LocalDate releaseDate;

    @Column(name = "has_romanian_sub")
    private boolean hasRomanianSub;

    @Column(name = "has_english_sub")
    private boolean hasEnglishSub;

    @Column(name = "runtime")
    private int runtime;

    @Column(name = "added_to_db_date")
    private LocalDate addedToDbDate;

    @Column(name = "internet_score")
    private BigDecimal internetScore;

    @Column(name = "internet_vote_count")
    private int internetVotesNumber;

    @Column(name = "imdb_Id")
    private String imdbID;

    @Column(name = "imdb_link")
    private String imdbLink;

    @Column(name = "tmdb_Id")
    private String tmdbID;

    @Column(name = "tmdb_poster_path")
    private String tmdbPosterPath;

    @Column(name = "youtube_Link")
    private String youtubeLink;

    @Column(name = "storage_name")
    private String storageName;

    @Column(name = "extern_fetching_status")
    private int externFetchingStatus;

    /**************************************** GENRE ******************************************/

    public Set<Genre> getGenres()
    {
        return this.genres;
    }

    public void addNewGenre(Genre genre)
    {
        if(this.genres == null)
        {
            this.genres = new HashSet<>();
        }

        genre.setVideo(this);
        this.genres.add(genre);
    }

    public void setGenres(Set<Genre> genres)
    {
        if(genres != null) {
            for(Genre genre : genres)
                genre.setVideo(this);
        }

        this.genres = genres;
    }

    /**************************************** COUNTRY ******************************************/

    public Set<Country> getCountries()
    {
        return this.countries;
    }

    public void addNewCountry(Country country)
    {
        if(this.countries == null)
        {
            this.countries = new HashSet<>();
        }

        country.setVideo(this);
        this.countries.add(country);
    }

    public void setCountries(Set<Country> countries)
    {
        if(countries != null) {
            for(Country country : countries)
                country.setVideo(this);
        }

        this.countries = countries;
    }

    /**************************************** LANGUAGE ******************************************/

    public List<Language> getLanguages()
    {
        return this.languages;
    }

    public void addNewLanguage(Language language)
    {
        if(this.languages == null)
        {
            this.languages = new ArrayList<Language>();
        }

        language.setVideo(this);
        this.languages.add(language);
    }

    public void setLanguages(List<Language> languages)
    {
        if(languages != null) {
            for(Language language : languages)
                language.setVideo(this);
        }

        this.languages = languages;
    }

    /**************************************** ACTOR ******************************************/

    public List<ActorJobs> getActors()
    {
        return this.actorJobs;
    }

    public void addActor(ActorJobs actor)
    {
        if(this.actorJobs == null)
        {
            this.actorJobs = new ArrayList<ActorJobs>();
        }

        actor.setVideo(this);
        this.actorJobs.add(actor);
    }

    public void setActors(List<ActorJobs> actors)
    {
        if(actors != null) {
            for(ActorJobs actor : actors)
                actor.setVideo(this);
        }

        this.actorJobs = actors;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getSecondTitle() { return secondTitle; }
    public void setSecondTitle(String secondTitle) { this.secondTitle = secondTitle; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getVideoType() { return videoType; }
    public void setVideoType(String videoType) { this.videoType = videoType; }

    public String getVideoStatus() { return videoStatus; }
    public void setVideoStatus(String videoStatus) { this.videoStatus = videoStatus; }

    public int getBudget() { return budget; }
    public void setBudget(int budget) { this.budget = budget; }

    public int getRevenue() { return revenue; }
    public void setRevenue(int revenue) { this.revenue = revenue; }

    public boolean getSawIt() { return sawIt; }
    public void setSawIt(boolean sawIt) { this.sawIt = sawIt; }

    public String getQuality() { return quality; }
    public void setQuality(String quality) { this.quality = quality; }

    public BigDecimal getPersonalScore() { return personalScore; }
    public void setPersonalScore(BigDecimal personalScore) { this.personalScore = personalScore; }

    public String getReleaseYear() { return releaseYear; }
    public void setReleaseYear(String releaseYear) { this.releaseYear = releaseYear; }

    public LocalDate getReleaseDate() { return releaseDate; }
    public void setReleaseDate(LocalDate releaseDate) { this.releaseDate = releaseDate; }

    public boolean getHasRomanianSub() { return hasRomanianSub; }
    public void setHasRomanianSub(boolean hasRomanianSub) { this.hasRomanianSub = hasRomanianSub; }

    public boolean getHasEnglishSub() { return hasEnglishSub; }
    public void setHasEnglishSub(boolean hasEnglishSub) { this.hasEnglishSub = hasEnglishSub; }

    public int getRuntime() { return runtime; }
    public void setRuntime(int runtime) { this.runtime = runtime; }

    public LocalDate getAddedToDbDate() { return addedToDbDate; }
    public void setAddedToDbDate(LocalDate addedToDbDate) { this.addedToDbDate = addedToDbDate; }

    public BigDecimal getInternetScore() { return internetScore; }
    public void setInternetScore(BigDecimal internetScore) { this.internetScore = internetScore; }

    public int getInternetVotesNumber() { return internetVotesNumber; }
    public void setInternetVotesNumber(int internetVotesNumber) { this.internetVotesNumber = internetVotesNumber; }

    public String getImdbID() { return imdbID; }
    public void setImdbID(String imdbID) { this.imdbID = imdbID; }

    public String getImdbLink() { return imdbLink; }
    public void setImdbLink(String imdbLink) { this.imdbLink = imdbLink; }

    public String getTmdbID() { return tmdbID; }
    public void setTmdbID(String tmdbID) { this.tmdbID = tmdbID; }

    public String getTmdbPosterPath() { return tmdbPosterPath; }
    public void setTmdbPosterPath(String tmdbPosterPath) { this.tmdbPosterPath = tmdbPosterPath; }

    public String getYoutubeLink() { return youtubeLink; }
    public void setYoutubeLink(String youtubeLink) { this.youtubeLink = youtubeLink; }

    public String getStorageName() { return storageName; }
    public void setStorageName(String storageName) { this.storageName = storageName; }

    public int getFetchingStatus() { return externFetchingStatus; }
    public void setFetchingStatus(int externFetchingStatus) { this.externFetchingStatus = externFetchingStatus; }
}
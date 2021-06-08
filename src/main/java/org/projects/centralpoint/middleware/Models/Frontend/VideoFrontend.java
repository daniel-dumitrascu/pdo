package org.projects.centralpoint.middleware.Models.Frontend;

import org.projects.centralpoint.middleware.Models.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public class VideoFrontend
{
    public Set<Genre> getGenres() { return this.genres; }
    public void setGenres(Set<Genre> genres) { this.genres = genres; }

    public Set<Country> getCountries() { return this.countries; }
    public void setCountries(Set<Country> countries) { this.countries = countries; }

    public List<Language> getLanguages() { return this.languages; }
    public void setLanguages(List<Language> languages) { this.languages = languages; }

    public List<ActorJobs> getActors() { return this.actorJobs; }
    public void setActors(List<ActorJobs> actors) { this.actorJobs = actors; }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

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

    public String getYoutubeLink() { return youtubeLink; }
    public void setYoutubeLink(String youtubeLink) { this.youtubeLink = youtubeLink; }

    public String getStorageName() { return storageName; }
    public void setStorageName(String storageName) { this.storageName = storageName; }

    public String getPoster() { return poster; }
    public void setPoster(String poster) { this.poster = poster; }


    private int id;
    private Set<Genre> genres;
    private Set<Country> countries;
    private List<Language> languages;
    private List<ActorJobs> actorJobs;
    private String title;
    private String description;
    private String videoType;
    private String videoStatus;
    private int budget;
    private int revenue;
    private boolean sawIt;
    private String quality = "unspecified";
    private BigDecimal personalScore;
    private String releaseYear;
    private LocalDate releaseDate;
    private boolean hasRomanianSub;
    private boolean hasEnglishSub;
    private int runtime;
    private LocalDate addedToDbDate;
    private BigDecimal internetScore;
    private int internetVotesNumber;
    private String imdbID;
    private String imdbLink;
    private String youtubeLink;
    private String storageName;
    private String poster;
}

package org.projects.centralpoint.middleware.Models;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "people")
public class Person
{
    @Id
    @Column(name = "people_key")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "nationality")
    private String nationality;

    @Column(name = "role")
    private int role;

    @Column(name = "email")
    private String email;

    @Column(name = "tmdb_id")
    private int tmdbId;

    @Column(name = "gender")
    private int gender;

    @Column(name = "birthday")
    private LocalDate birthday;

    @Column(name = "birthplace")
    private String birthplace;

    @Column(name = "deathday")
    private LocalDate deathday;

    @Column(name = "biography")
    private String biography;

    @Column(name = "tmdb_poster_path")
    private String tmdbPosterPath;

    @Column(name = "extern_fetching_status")
    private int externFetchingStatus;

    @OneToOne(mappedBy = "person", cascade = CascadeType.ALL, orphanRemoval = true)
    private User user;

    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ActorJobs> actorJobs;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getNationality() { return nationality; }
    public void setNationality(String nationality) { this.nationality = nationality; }

    public int getRole() { return role; }
    public void setRole(int role) { this.role = role; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public List<ActorJobs> getActorJobs() { return actorJobs; }
    public void setActorJobs(List<ActorJobs> actorJobs) { this.actorJobs = actorJobs; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public int getTmdbId() { return tmdbId; }
    public void setTmdbId(int tmdbId) { this.tmdbId = tmdbId; }

    public int getGender() { return gender; }
    public void setGender(int gender) { this.gender = gender; }

    public LocalDate getBirthday() { return birthday; }
    public void setBirthday(LocalDate birthday) { this.birthday = birthday; }

    public String getBirthplace() { return birthplace; }
    public void setBirthplace(String birthplace) { this.birthplace = birthplace; }

    public LocalDate getDeathday() { return deathday; }
    public void setDeathday(LocalDate deathday) { this.deathday = deathday; }

    public String getBiography() { return biography; }
    public void setBiography(String biography) { this.biography = biography; }

    public String getTmdbPosterPath() { return tmdbPosterPath; }
    public void setTmdbPosterPath(String tmdbPosterPath) { this.tmdbPosterPath = tmdbPosterPath; }

    public int getFetchingStatus() { return externFetchingStatus; }
    public void setFetchingStatus(int externFetchingStatus) { this.externFetchingStatus = externFetchingStatus; }
}

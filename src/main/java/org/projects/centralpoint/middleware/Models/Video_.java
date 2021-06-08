package org.projects.centralpoint.middleware.Models;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import java.time.LocalDate;


@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Video.class)
public abstract class Video_
{
    public static volatile SetAttribute<Video, Genre> genres;
    public static volatile SetAttribute<Video, Country> countries;
    public static volatile ListAttribute<Video, Language> languages;
    public static volatile ListAttribute<Video, ActorJobs> actorJobs;
    public static volatile SingularAttribute<Video, Integer> id;
    public static volatile SingularAttribute<Video, String> title;
    public static volatile SingularAttribute<Video, String> secondTitle;
    public static volatile SingularAttribute<Video, String> description;
    public static volatile SingularAttribute<Video, String> videoType;
    public static volatile SingularAttribute<Video, String> videoStatus;
    public static volatile SingularAttribute<Video, Integer> budget;
    public static volatile SingularAttribute<Video, Integer> revenue;
    public static volatile SingularAttribute<Video, Boolean> sawIt;
    public static volatile SingularAttribute<Video, String> quality;
    public static volatile SingularAttribute<Video, Float> personalScore;
    public static volatile SingularAttribute<Video, String> releaseYear;
    public static volatile SingularAttribute<Video, LocalDate> releaseDate;
    public static volatile SingularAttribute<Video, Boolean> hasRomanianSub;
    public static volatile SingularAttribute<Video, Boolean> hasEnglishSub;
    public static volatile SingularAttribute<Video, Integer> runtime;
    public static volatile SingularAttribute<Video, LocalDate> addedToDbDate;
    public static volatile SingularAttribute<Video, Float> internetScore;
    public static volatile SingularAttribute<Video, Integer> internetVotesNumber;
    public static volatile SingularAttribute<Video, String> imdbID;
    public static volatile SingularAttribute<Video, String> imdbLink;
    public static volatile SingularAttribute<Video, String> tmdbID;
    public static volatile SingularAttribute<Video, String> tmdbPosterPath;
    public static volatile SingularAttribute<Video, String> youtubeLink;
    public static volatile SingularAttribute<Video, String> storageName;
    public static volatile SingularAttribute<Video, Integer> externFetchingStatus;
}

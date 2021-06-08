package org.projects.centralpoint.middleware.Models;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "video_genres")
public class Genre
{
    /*public Genre(String genre)
    {
        setGenre(genre);
    }*/

    @Id
    @Column(name = "video_genre_key")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="video_key")
    private Video video;

    @Column(name = "genre", nullable = false)
    private String genre;

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }

    public void setVideo(Video video) { this.video = video; }

    @Override
    public boolean equals(Object object)
    {
        boolean isEqual= false;

        if (object != null && object instanceof Genre)
        {
            isEqual = ((Genre)object).getGenre().equals(this.getGenre());
        }

        return isEqual;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(genre);
    }
}

package org.projects.centralpoint.middleware.Models;

import javax.persistence.*;

@Entity
@Table(name = "video_language")
public class Language
{
    /*public Language(String language)
    {
        setLanguage(language);
    }*/

    @Id
    @Column(name = "video_language_key")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="video_key")
    private Video video;

    @Column(name = "language", nullable = false)
    private String language;

    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }

    public void setVideo(Video video) { this.video = video; }

    @Override
    public boolean equals(Object object)
    {
        boolean isEqual= false;

        if (object != null && object instanceof Language)
        {
            isEqual = ((Language)object).getLanguage().equals(this.getLanguage());
        }

        return isEqual;
    }

    //TODO why do I need to overwrite the hashCode if I overwrite the equals?
//    @Override
//    public int hashCode() {
//        return this.getGenre();
//    }
}

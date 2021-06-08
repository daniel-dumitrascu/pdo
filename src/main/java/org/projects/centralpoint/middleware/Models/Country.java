package org.projects.centralpoint.middleware.Models;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "video_country")
public class Country
{
    /*public Country(String country)
    {
        setCountry(country);
    }*/

    @Id
    @Column(name = "video_country_key")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="video_key")
    private Video video;

    @Column(name = "country", nullable = false)
    private String country;

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public void setVideo(Video video) { this.video = video; }

    @Override
    public boolean equals(Object object)
    {
        boolean isEqual= false;

        if (object != null && object instanceof Country)
        {
            isEqual = ((Country)object).getCountry().equals(this.getCountry());
        }

        return isEqual;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(country);
    }
}

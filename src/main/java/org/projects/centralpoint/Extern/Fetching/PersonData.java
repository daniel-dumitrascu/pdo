package org.projects.centralpoint.Extern.Fetching;

import java.time.LocalDate;
import org.projects.centralpoint.Defines.Types;

public class PersonData
{
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Types.PeopleGender getGender() { return gender; }
    public void setGender(Types.PeopleGender gender) { this.gender = gender; }

    public LocalDate getBirthday() { return birthday; }
    public void setBirthday(LocalDate date) { this.birthday = date; }

    public LocalDate getDeathday() { return deathday; }
    public void setDeathday(LocalDate date) { this.deathday = date; }

    public String getBiography() { return biography; }
    public void setBiography(String biography) { this.biography = biography; }

    public String getImagePath() { return imagePath; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }

    public String getPlaceOfBirth() { return placeOfBirth; }
    public void setPlaceOfBirth(String placeOfBirth) { this.placeOfBirth = placeOfBirth; }

    private String      name;
    private int         id;
    private Types.PeopleGender      gender;
    private LocalDate   birthday;
    private LocalDate   deathday;
    private String      biography;
    private String      imagePath;
    private String      placeOfBirth;
}

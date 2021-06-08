package org.projects.centralpoint.Defines;


public class Types
{
    public enum EntityType
    {
        UNSPECIFIED, VIDEO, APP, BOOK, PHOTO, MUSIC
    }

    public enum PeopleRole
    {
        ACTOR(0), DIRECTOR(1), WRITER(2), SINGER(3), USER(4);

        private int value;

        PeopleRole(final int newValue)
        {
            if(newValue >= 0 && newValue <= 4)
                value = newValue;
        }

        public int getIntValue() { return value; }
    }

    public enum PeopleGender
    {
        MALE(0), FEMALE(1);

        private int value;

        PeopleGender(final int newValue)
        {
            if(newValue >= 0 && newValue <= 1)
                value = newValue;
        }

        public int getIntValue() { return value; }
    }

    public enum AppType
    {
        GAME, ARCHIVE, NETWORK, OFFICE
    }

    public enum GameType
    {
        UNSPECIFIED, STRATEGY, PUZZLE, RPG, SHOOTER, PLATFORMER, RACING, ADVENTURE
    }

    public enum PhotoType
    {
        UNSPECIFIED, PERSONAL, HISTORY, ART, NATURE
    }

    public enum BookType
    {
        UNSPECIFIED, BIBLIOGRAPHY, DRAMA, COMEDY, MYSTERY, HORROR, SF, FANTASY, HISTORY
    }

    public enum FileType
    {
        UNSPECIFIED, MP3, WAVE
    }

    public enum  MusicType
    {
        UNSPECIFIED, AMBIENT, ROCK, HOUSE, JAZZ, RAP
    }

    public enum Platform
    {
        UNSPECIFIED, PC, LINUX, MAC, PS3, PS4, ANDROID
    }

    public enum LicenceType
    {
        UNSPECIFIED, FREE, FREE_NON_COMMERCIAL, COMMERCIAL
    }
}


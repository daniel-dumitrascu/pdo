package org.projects.centralpoint.Defines;

public final class VideoGenres
{
    /*
     * The order in the enum must be the same as the order of the genres in the array.
     * So, we must be very careful when placing a new genre in the array or enum.
     * We should place it only at the end.
     */

    public enum GenreIndex
    {
        ACTION,
        ADVENTURE,
        ANIMATION,
        COMEDY,
        CRIME,
        DRAMA,
        HORROR,
        MYSTERY,
        ROMANCE,
        WAR,
        WESTERN,
        THRILLER,
        BIOGRAPHY,
        SF,
        FANTASY,
        HISTORY
    }

    public static String[] getAllGenres() { return genres; }
    public static String getGenreByIndex(GenreIndex ind) { return genres[ind.ordinal()]; }

    private static final String[] genres = {"action", "adventure", "animation", "comedy", "crime",
                                            "drama", "horror", "mystery","romance", "war",
                                            "western", "thriller", "biography", "sf", "fantasy",
                                            "history"};
}

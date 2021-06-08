package org.projects.centralpoint.middleware.Services;

import org.projects.centralpoint.Filter.SearchFilter;
import org.projects.centralpoint.middleware.Models.Video;

import java.util.List;

public interface VideoService
{
    void SaveMovie(Video video);

    void DeleteMovieByName(String movieName);

    List<Video> GetAllVideosByFilter(SearchFilter searchFilter, boolean fetchAdditionalInfo);

    List<Video> GetWaitingVideos();

    void UpdateMovie(Video video);

    void updateVideo(Video video);

    void updateVideos(List<Video> videos);

    boolean CheckIfVideoExist(String videoName, String releaseYear, String storedLocation);

    Video GetVideoById(int id);
}

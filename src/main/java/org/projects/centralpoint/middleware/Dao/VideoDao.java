package org.projects.centralpoint.middleware.Dao;

import org.projects.centralpoint.Filter.SearchFilter;
import org.projects.centralpoint.middleware.Models.Video;

import java.util.List;

public interface VideoDao
{
    void SaveVideo(Video video);

    void DeleteVideoByName(String videoName);

    List<Video> GetAllVideosByFilter(SearchFilter searchFilter, boolean fetchAdditionalInfo);

    List<Video> GetWaitingVideos();

    void updateVideo(Video video);

    void updateVideos(List<Video> videos);

    Video GetVideoById(int id);
}

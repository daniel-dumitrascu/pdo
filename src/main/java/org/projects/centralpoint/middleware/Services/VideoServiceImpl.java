package org.projects.centralpoint.middleware.Services;

import org.projects.centralpoint.Defines.Types;
import org.projects.centralpoint.middleware.Dao.VideoDao;
import org.projects.centralpoint.middleware.Dao.ActorDao;
import org.projects.centralpoint.Filter.SearchFilter;
import org.projects.centralpoint.middleware.Models.ActorJobs;
import org.projects.centralpoint.middleware.Models.Video;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("videoService")
@Transactional
public class VideoServiceImpl implements VideoService
{
    @Autowired
    private VideoDao videoDao;

    @Autowired
    private ActorDao actorDao;

    public void SaveMovie(Video video)
    {
        // First, save the video
        videoDao.SaveVideo(video);

        // Then, save the actorJobs but only the
        // ones that we don't have in the DB
        List<ActorJobs> actorJobs = video.getActors();
        if(actorJobs != null)
        {
            for(ActorJobs job : actorJobs)
            {
                if(!actorDao.actorExists(job))
                {
                    actorDao.SaveActor(job);
                }
            }
        }
    }

    public void DeleteMovieByName(String movieName) { videoDao.DeleteVideoByName(movieName); }

    public List<Video> GetAllVideosByFilter(SearchFilter searchFilter, boolean fetchAdditionalInfo) { return videoDao.GetAllVideosByFilter(searchFilter, fetchAdditionalInfo); }

    public List<Video> GetWaitingVideos() { return videoDao.GetWaitingVideos(); }

    public void UpdateMovie(Video video) { videoDao.updateVideo(video); }

    public void updateVideo(Video video)
    {
        videoDao.updateVideo(video);
    }

    public void updateVideos(List<Video> videos) { videoDao.updateVideos(videos); }

    public boolean CheckIfVideoExist(String videoName, String releaseYear, String storedLocation)
    {
        SearchFilter filter = new SearchFilter(Types.EntityType.VIDEO);
        filter.AddFilter(SearchFilter.FilterType.FILTER_NAME, videoName);

        if(releaseYear != null && !releaseYear.isEmpty())
            filter.AddFilter(SearchFilter.FilterType.FILTER_YEAR, releaseYear);

        if(storedLocation != null && !storedLocation.isEmpty())
            filter.AddFilter(SearchFilter.FilterType.FILTER_STORED_LOCATION, storedLocation);

        filter.TurnFilterSettingsON(SearchFilter.FilterSettings.FILTER_EXACT_TITLE);

        List<Video> videos = GetAllVideosByFilter(filter, false);

        return (videos != null && videos.size() > 0);
    }

    public Video GetVideoById(int id)
    {
        return videoDao.GetVideoById(id);
    }
}

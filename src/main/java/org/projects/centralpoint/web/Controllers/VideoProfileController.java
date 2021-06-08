package org.projects.centralpoint.web.Controllers;

import java.util.ArrayList;
import java.util.List;
import org.projects.centralpoint.Utils.Resources.ResUtils;
import org.projects.centralpoint.middleware.Models.Factory.ModelFactory;
import org.projects.centralpoint.middleware.Models.Frontend.VideoFrontend;
import org.projects.centralpoint.middleware.Models.Frontend.VideoResponse;
import org.projects.centralpoint.middleware.Models.Video;
import org.projects.centralpoint.middleware.Services.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.google.gson.Gson;

@RestController
public class VideoProfileController
{
    @Autowired
    private VideoService videoService;

    @Autowired
    private ResUtils resUtils;

    @CrossOrigin(origins = "http://localhost:3100")
    @RequestMapping(value = "/video/profile/{id}", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public String VideoProfileGetHandler(@PathVariable("id") int id)
    {
        VideoResponse response = new VideoResponse();
        response.setRequestType("profile");
        Gson gson = new Gson();

        // We now have a video ID. With it we can retrieve the whole
        // data for that specific video entry.
        Video video = videoService.GetVideoById(id);
        if(video != null) {
            // We calculate the local poster path and set that into the Video model
            // so that we don't have to send it into ConvertModelToFrontend
            // as an extra parameter
            video.setTmdbPosterPath(resUtils.getVideoPosterPath(video));
            VideoFrontend fv = (VideoFrontend) ModelFactory.GetInstance().ConvertModelToFrontend(video, video.getVideoType());
            List<VideoFrontend> frontendVideos = new ArrayList<>(); //TODO this is a logical problem because this request will always return only one model and not more
                                                                    // For this solution you need to modify VideoResponse so that it is more general and can return
                                                                    // a list when needed or a simple model as in this case
            frontendVideos.add(fv);
            response.setVideos(frontendVideos);
            response.setStatus("success");
        } else {
            response.setMsg("profile page wasn't found");
            response.setStatus("fail");
        }

        String gg = gson.toJson(response);
        return gg;
    }
}

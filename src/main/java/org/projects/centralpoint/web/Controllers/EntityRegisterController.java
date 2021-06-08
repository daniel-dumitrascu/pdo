package org.projects.centralpoint.web.Controllers;

import com.google.gson.Gson;
import org.projects.centralpoint.Extern.ExternDataEntity;
import org.projects.centralpoint.Extern.ExternDataManager;

import org.projects.centralpoint.middleware.Models.Video;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@RestController
public class EntityRegisterController
{
    @Autowired
    org.projects.centralpoint.middleware.Services.VideoService videoService;

    @Autowired
    private ExternDataManager externDataManager;

    @CrossOrigin(origins = "http://localhost:3100")
    @RequestMapping(value = "/video/register", method = RequestMethod.POST,
                    produces = MediaType.APPLICATION_JSON_VALUE)
    public String EntityRegisterPostHandler(@RequestBody Map<String, Object> jsonDataMap)
    {
        String infoMsg = new String();
        Map<String, Object> response = new HashMap<>();
        Gson gson = new Gson();

        String videoTitle = (String)jsonDataMap.get("videoTitle");
        String videoType = (String)jsonDataMap.get("videoType");
        boolean videoSawIt = (Boolean) jsonDataMap.get("videoSawIt");
        boolean videoHasRomanianSub = (Boolean) jsonDataMap.get("videoHasRomanianSub");
        boolean videoHasEnglishSub = (Boolean) jsonDataMap.get("videoHasEnglishSub");
        String videoQuality = (String)jsonDataMap.get("videoQuality");
        String videoPersonalScore = (String) jsonDataMap.get("videoPersonalScore");
        String videoReleaseYear = (String)jsonDataMap.get("videoReleaseYear");
        String videoStored = (String)jsonDataMap.get("videoStored");

        // Is the name valid?
        if(videoTitle == null || videoTitle.isEmpty()) {
            response.put("registration", "fail");
            return gson.toJson(response);
        }

        if(videoType == null || videoType.isEmpty()) {
            videoType = "undefined";
        }

        if(videoService.CheckIfVideoExist(videoTitle, videoReleaseYear, videoStored) == false)
        {
            Video video = new Video();
            video.setTitle(videoTitle);
            video.setVideoType(videoType);
            video.setSawIt(videoSawIt);
            video.setHasEnglishSub(videoHasEnglishSub);
            video.setHasRomanianSub(videoHasRomanianSub);

            if(videoQuality.compareTo("excellent") == 0 || videoQuality.compareTo("good") == 0 ||
                 videoQuality.compareTo("average") == 0 || videoQuality.compareTo("bad") == 0)
            {
                video.setQuality(videoQuality);
            }
            else
                video.setQuality("unspecified");

            try {
                float score = Float.parseFloat(videoPersonalScore);
                if(score > 0.0)
                    video.setPersonalScore(new BigDecimal("" + score));
            } catch(Exception e) {
                System.out.println("[ERROR] There was a problem in converting the internet score value to float: " + e);
            }

            video.setReleaseYear(videoReleaseYear);
            video.setStorageName(videoStored);

            videoService.SaveMovie(video);

            // We should let know the thread that fetches external data about movies
            // that there are movies in the database that need taking care of
            externDataManager.workAvailableWarning(ExternDataEntity.VIDEO);
            externDataManager.workAvailableWarning(ExternDataEntity.PEOPLE);

            response.put("registration", "success");
            response.put("info", "the video was successfully registered");
        }
        else
        {
            response.put("registration", "fail");
            response.put("info", "there's already a video in the database with this name");
        }

        return gson.toJson(response);
    }

    /*@RequestMapping(value = "/video/register", method = RequestMethod.POST)
    public String EntityRegisterPostHandler(String videoTitle, String videoType,
                                            boolean videoSawIt, boolean videoHasRomanianSub,
                                            boolean videoHasEnglishSub, String videoQuality, float videoPersonalScore,
                                            String videoReleaseYear, String videoStored, ModelMap model)
    {
        boolean errorDetected = false;
        String infoMsg = new String();

        if(videoTitle != null && !videoTitle.isEmpty() &&
           videoType != null && !videoType.isEmpty())
        {
            if(videoService.CheckIfVideoExist(videoTitle, videoReleaseYear, videoStored) == false)
            {
                Video video = new Video();

                video.setTitle(videoTitle);

                if(videoType.compareTo("movie") == 0 || videoType.compareTo("series") == 0 ||
                   videoType.compareTo("doc") == 0 || videoType.compareTo("other") == 0)
                {
                    video.setVideoType(videoType);
                }

                video.setSawIt(videoSawIt);
                video.setHasEnglishSub(videoHasEnglishSub);
                video.setHasRomanianSub(videoHasRomanianSub);

                if(videoQuality.compareTo("excellent") == 0 || videoQuality.compareTo("good") == 0 ||
                   videoQuality.compareTo("average") == 0 || videoQuality.compareTo("bad") == 0)
                {
                    video.setQuality(videoQuality);
                }
                else
                {
                    video.setQuality("unspecified");
                }

                if(videoPersonalScore > 0.0)
                    video.setPersonalScore(new BigDecimal("" + videoPersonalScore));

                video.setReleaseYear(videoReleaseYear);
                video.setStorageName(videoStored);

                videoService.SaveMovie(video);

                // We should let know the thread that fetches external data about movies
                // that there are movies in the database that need taking care of
                externDataManager.workAvailableWarning(ExternDataEntity.VIDEO);
                externDataManager.workAvailableWarning(ExternDataEntity.PEOPLE);

                infoMsg = "the video was successfully registered";
            }
            else
            {
                errorDetected = true;
                infoMsg = "there's already a video in the database with this name";
            }

            model.put("errorDetected", errorDetected);
            model.put("infoMsg", infoMsg);
        }

        return "entity_register";
    }*/
}

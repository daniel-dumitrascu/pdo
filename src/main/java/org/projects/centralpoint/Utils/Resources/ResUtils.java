package org.projects.centralpoint.Utils.Resources;

import org.apache.commons.io.FilenameUtils;
import org.projects.centralpoint.Settings.AppSettings;
import org.projects.centralpoint.middleware.Models.Video;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Component
public class ResUtils
{
    @Autowired
    private AppSettings appSettings;

    public String getVideoPosterPath(Video video)
    {
        String path = "/assets/default_movie_poster.jpg";

        if(video != null && video.getId() > 0 &&
           video.getTmdbPosterPath() != null &&
           !video.getTmdbPosterPath().isEmpty())
        {
            String fileName = video.getId() + "_poster." + FilenameUtils.getExtension(video.getTmdbPosterPath());
            String absolutePosterPath = appSettings.getDbResourcesPath() + "/movies/posters/" + fileName;

            /*
             * Do we have this poster on our pc ?
             */
            File posterFile = new File(absolutePosterPath);
            if(posterFile.exists() && !posterFile.isDirectory())
            {
                InputStream inputStream = getClass().getClassLoader().getResourceAsStream("application.properties");
                Properties prop = new Properties();
                try
                {
                    prop.load(inputStream);
                    inputStream.close();
                }
                catch(IOException e)
                {
                    System.out.println("[ERROR] Can not retrieve values from application.properties");
                    return path;
                }

                path = "http://" + prop.getProperty("http.server.ip") + ":" + prop.getProperty("http.server.port") +
                        "/db_resources/movies/posters/" + fileName;
            }
        }

        return path;
    }
}

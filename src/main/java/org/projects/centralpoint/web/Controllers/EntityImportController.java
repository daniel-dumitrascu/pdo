package org.projects.centralpoint.web.Controllers;

import org.projects.centralpoint.Extern.ExternDataEntity;
import org.projects.centralpoint.Extern.ExternDataManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.ui.ModelMap;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.projects.centralpoint.Imported.FileImporter;
import org.projects.centralpoint.middleware.Services.VideoService;
import org.projects.centralpoint.middleware.Models.Video;
import org.projects.centralpoint.Defines.*;
import com.google.gson.Gson;

@Controller
public class EntityImportController
{
    @Autowired
    private VideoService videoService;

    @Autowired
    private ExternDataManager externDataManager;

    @Bean(name = "multipartResolver")
    public CommonsMultipartResolver createMultipartResolver() {
        CommonsMultipartResolver resolver=new CommonsMultipartResolver();
        resolver.setDefaultEncoding("utf-8");
        return resolver;
    }

    @CrossOrigin(origins = "http://localhost:3100")
    @RequestMapping(value = "/video/import", headers = "content-type=multipart/*", method = RequestMethod.POST)
    public String ImportPostHandler(@RequestParam("browseButton") MultipartFile browseButton, ModelMap modelMap) throws IOException
    {
        Map<String, Object> response = new HashMap<>();
        Gson gson = new Gson();
        if(!browseButton.isEmpty())
        {
            InputStream fileContent = browseButton.getInputStream();
            FileImporter importer = new FileImporter();
            List<Object> entities = importer.ConstructEntities(fileContent, Types.EntityType.VIDEO);

            if(entities != null && entities.size() > 0)
            {
                if(entities.get(0) instanceof Video)
                {
                    int entityCount = 1;
                    for(Object entity : entities)
                    {
                        System.out.print("#" + entityCount++ + " Saving entity '" + ((Video)entity).getTitle() + "': ");
                        if(!videoService.CheckIfVideoExist(((Video)entity).getTitle(),
                                                           ((Video)entity).getReleaseYear(),
                                                           ((Video)entity).getStorageName()))
                        {
                            videoService.SaveMovie((Video)entity);
                            System.out.println("DONE");
                        }
                        else
                        {
                            System.out.println("The video '" + ((Video)entity).getTitle() + "' is already in the database");
                        }
                    }

                    // We should let know the thread that fetches external data about movies
                    // that there are movies in the database that need taking care of
                    externDataManager.workAvailableWarning(ExternDataEntity.VIDEO);
                    externDataManager.workAvailableWarning(ExternDataEntity.PEOPLE);
                    
                    response.put("registration", "success");
                    return gson.toJson(response);
                }
               /* else if(dataCollection.GetDataType() == Types.EntityType.APP)
                {
                    // Handle the imported app entities
                }
                else if(dataCollection.GetDataType() == Types.EntityType.BOOK)
                {
                    // Handle the imported book entities
                }
                else if(dataCollection.GetDataType() == Types.EntityType.MUSIC)
                {
                    // Handle the imported music entities
                }
                else if(dataCollection.GetDataType() == Types.EntityType.PHOTO)
                {
                    // Handle the imported photo entities
                }*/
            }
        }

        response.put("import", "fail");
        response.put("info", "there was a problem when trying to parse the uploaded file");
        return gson.toJson(response);
    }
}
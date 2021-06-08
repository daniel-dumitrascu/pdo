package org.projects.centralpoint.Imported;

import org.projects.centralpoint.File.Serializer.Deserializer;
import org.projects.centralpoint.middleware.Models.Video;
import org.projects.centralpoint.Defines.Types;
import org.projects.centralpoint.middleware.Helper.ModelHelper;
import org.projects.centralpoint.middleware.Helper.VideoModelHelper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.List;



public class FileImporter
{
    private Logger logger = LoggerFactory.getLogger(FileImporter.class);

    public void FileImporter()
    {

    }

    public List<Object> ConstructEntities(InputStream fileStream, Types.EntityType fileContentType)
    {
        return ConstructEntitiesFromDeserializer(new Deserializer(fileStream, fileContentType));
    }

    public List<Object> ConstructEntities(String filePath, Types.EntityType fileContentType)
    {
        return ConstructEntitiesFromDeserializer(new Deserializer(filePath, fileContentType));
    }

    private List<Object> ConstructEntitiesFromDeserializer(Deserializer deserializer)
    {
        List<Object> entities = null;

        if(deserializer != null)
        {
            entities = deserializer.Deserialize();
        }

        return entities;
    }
}

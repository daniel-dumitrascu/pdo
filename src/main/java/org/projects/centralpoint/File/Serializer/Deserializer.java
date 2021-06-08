package org.projects.centralpoint.File.Serializer;

import org.projects.centralpoint.File.Loader.FileLoader;
import org.projects.centralpoint.Defines.Types;
import org.projects.centralpoint.middleware.Models.Factory.ModelFactory;
import org.projects.centralpoint.middleware.Models.Video;
import org.projects.centralpoint.File.Loader.Cell;

import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Deserializer
{
    public Deserializer(String filePath, Types.EntityType fileContentType)
    {
        fileLoader = new FileLoader(filePath);
        this.fileContentType = fileContentType;
    }

    public Deserializer(InputStream fileStream, Types.EntityType fileContentType)
    {
        fileLoader = new FileLoader(fileStream);
        this.fileContentType = fileContentType;
    }

    public List<Object> Deserialize()
    {
        List<Object> entitiesCollection = null;

        switch (fileContentType)
        {
            case VIDEO:
            {
                entitiesCollection = DeserializeVideo();
                break;
            }
            /*case APP:
            {
                entitiesCollection = DeserializeApp();
                break;
            }
            case BOOK:
            {
                entitiesCollection = DeserializeBook();
                break;
            }
            case MUSIC:
            {
                entitiesCollection = DeserializeMusic();
                break;
            }
            case PHOTO:
            {
                entitiesCollection = DeserializePhoto();
                break;
            }*/
            case UNSPECIFIED:
            {
                //TODO a similar error msg must be printed in the page
                // this msg must be printed in logs as well and not at the console
                System.out.println("The file can't be imported because the format of it is unknown.");
            }
        }

        return entitiesCollection;
    }

    public List<Object> DeserializeVideo()
    {
        List<List<Cell>> dataCollection = fileLoader.Parse();
        List<Object> importedData = new ArrayList<Object>();

        for(int i = 0; i < dataCollection.size(); ++i)
        {
            List<Cell> cells = dataCollection.get(i);
            Cell ModelTypeCell = null;

            for(Cell cell : cells)
            {
                if(cell.GetCellTitle().contains("Title Type"))
                    ModelTypeCell = cell;
            }

            /* If type was found then we can create a new model and we can add it to the data collection */
            Video model = (Video)ModelFactory.GetInstance().CreateModel((String)ModelTypeCell.GetCellData(), cells);

            if(model != null)
            {
                model.setAddedToDbDate(LocalDate.now());
                importedData.add(model);
            }
        }

        return importedData;
    }

    private List<Object> DeserializeApp() { return null; }

    private List<Object> DeserializeBook()
    {
        return null;
    }

    private List<Object> DeserializeMusic()
    {
        return null;
    }

    private List<Object> DeserializePhoto()
    {
        return null;
    }

    private FileLoader fileLoader;
    private Types.EntityType fileContentType;
}

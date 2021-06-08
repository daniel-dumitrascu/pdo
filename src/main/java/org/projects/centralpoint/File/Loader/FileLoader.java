package org.projects.centralpoint.File.Loader;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.projects.centralpoint.Utils.String.StringHelper;


public class FileLoader
{
    public FileLoader(String filePath)
    {
        Init();

        if(!filePath.isEmpty())
        {
            isFileLoaded = LoadFile(filePath);
        }
    }

    public FileLoader(InputStream fileStream)
    {
        Init();

        if(fileStream != null)
        {
            isFileLoaded = LoadFile(fileStream);
        }
    }

    public List<List<Cell>> Parse()
    {
        List<List<Cell>> dataCollection = new ArrayList<List<Cell>>();

        if(isFileLoaded && !fileText.isEmpty())
        {
            Scanner scannedText = new Scanner(fileText);

            if(!scannedText.hasNextLine()){ return null; }

            /*
             * Get the file header and check if we have all the columns available in the file.
             */
            String columnHeader = scannedText.nextLine();
            List<String> columns = StringHelper.TokenizeByDelimiter(columnHeader, ",", false);

            List<ColumnInfo> columnInfoColl = new ArrayList<>();

            /*
             * Add the mandatory columns and also check if all of them are present
             */
            for (String column : mandatoryColumns)
            {
                int index = columns.indexOf(column);
                if(index == -1) { return null; }

                columnInfoColl.add(new ColumnInfo(column,index));
            }

            /*
             * Add the present optionally columns
             */
            for (String column : optionalColumns)
            {
                int index = columns.indexOf(column);
                if(index == -1) { continue; }

                columnInfoColl.add(new ColumnInfo(column,index));
            }

            /*
             * Get the actual information from the file
             */
            while (scannedText.hasNextLine())
            {
                String line = scannedText.nextLine();

                String[] rowDataColl = line.split("(?x)   " +
                                                ",          " +   // Split on comma
                                                "(?=        " +   // Followed by
                                                "  (?:      " +   // Start a non-capture group
                                                "    [^\"]* " +   // 0 or more non-quote characters
                                                "    \"     " +   // 1 quote
                                                "    [^\"]* " +   // 0 or more non-quote characters
                                                "    \"     " +   // 1 quote
                                                "  )*       " +   // 0 or more repetition of non-capture group (multiple of 2 quotes will be even)
                                                "  [^\"]*   " +   // Finally 0 or more non-quotes
                                                "  $        " +   // Till the end  (This is necessary, else every comma will satisfy the condition)
                                                ")          "     // End look-ahead
                );

                List<Cell> lineData = new ArrayList<Cell>();

                /* Add the data to the collection */
                for (ColumnInfo collInfo : columnInfoColl)
                {
                    if(collInfo.columnIndex < rowDataColl.length &&
                       !rowDataColl[collInfo.columnIndex].isEmpty())
                    {
                        if(collInfo.columnName.equals("IMDb Rating"))
                        {
                            try
                            {
                                lineData.add(new Cell(collInfo.columnName,
                                        BigDecimal.class.getName(),
                                        new BigDecimal(rowDataColl[collInfo.columnIndex])));
                            }
                            catch (NumberFormatException nfe)
                            {
                                //TODO login error message
                            }
                        }
                        else if(collInfo.columnName.equals("Num Votes") ||
                                collInfo.columnName.equals("Runtime (mins)"))
                        {
                            Integer value = null;
                            try
                            {
                                value = Integer.parseInt(rowDataColl[collInfo.columnIndex]);
                                lineData.add(new Cell(collInfo.columnName, Integer.class.getName(), value));
                            }
                            catch (NumberFormatException nfe)
                            {
                                //TODO login error message
                            }
                        }
                        else
                        {
                            lineData.add(new Cell(collInfo.columnName, String.class.getName(), rowDataColl[collInfo.columnIndex]));
                        }
                    }
                }

                if(lineData.size() > 0)
                    dataCollection.add(lineData);
            }
        }

        return dataCollection;
    }

    /*
     * ColumnInfo is a helper class that binds together a column name with a column index
     * The index represents the position of the column in the file header
     */

    private class ColumnInfo
    {
        public ColumnInfo(String columnName, int columnIndex)
        {
            this.columnName = columnName;
            this.columnIndex = columnIndex;
        }

        public String columnName;
        public int columnIndex;
    }

    private void Init()
    {
        /* The columns that present interest for us are:
         *
         * Const - this stores the IMDB ID
         * Title - the movie title
         * URL - the IMDB link to the movie page
         * Title Type - the video type which can be "movie", "tvSeries", etc
         * IMDB Rating - the IMDB rating
         * Runtime (mins) - the runtime of the video
         * Year - the year when the movie appeared
         * Genres - the video genres
         * Num Votes - number of IMDB votes
         * Release Date - the date in which the movie was released
         * Directors - a list of movie directors for the current video
         *
         * Din coloanele prezentate mai sus avem cateva care sunt obligatorii.
         * Daca cumva una dintre acestea lipseste atunci nu vom parsa acea linie ci vom outputa
         * intr-un fisier de log toate intrarile incomplete. Coloanele obligatorii sunt:
         *
         * Const, Title, Title Type
         */

        mandatoryColumns = new String[] {"Const", "Title", "Title Type"};
        optionalColumns = new String[] {"URL", "IMDb Rating", "Runtime (mins)", "Year", "Genres",
                                        "Num Votes", "Release Date", "Directors", "Storing location"};
    }

    private boolean LoadFile(String filePath)
    {
        UnloadFile();
        boolean loadindStatus = false;
        InputStream fileImportParserStream = null;

        try
        {
            fileImportParserStream = new FileInputStream(filePath);
            fileText = IOUtils.toString(fileImportParserStream, "UTF-8");
            loadindStatus = true;
        }
        catch (FileNotFoundException e)
        {
            logger.error("File (" + filePath + ") could not be loaded.");
            e.printStackTrace();
        }
        catch (IOException e)
        {
            logger.error("The conversion of the file to string text was not possible.");
            e.printStackTrace();
        }
        finally
        {
            IOUtils.closeQuietly(fileImportParserStream);
        }

        return loadindStatus;
    }

    private boolean LoadFile(InputStream fileStream)
    {
        UnloadFile();
        boolean loadindStatus = false;

        try
        {
            fileText = IOUtils.toString(fileStream, "UTF-8");
            loadindStatus = true;
        }
        catch (IOException e)
        {
            logger.error("The conversion of the file to string text was not possible.");
            e.printStackTrace();
        }
        finally
        {
            IOUtils.closeQuietly(fileStream);
        }

        return loadindStatus;
    }

    private void UnloadFile()
    {
        if(isFileLoaded)
        {
            isFileLoaded = false;
            fileText = "";
        }
    }

    private String[] mandatoryColumns;
    private String[] optionalColumns;
    private String fileText;
    private boolean isFileLoaded = false;
    private Logger logger = LoggerFactory.getLogger(FileLoader.class);
}

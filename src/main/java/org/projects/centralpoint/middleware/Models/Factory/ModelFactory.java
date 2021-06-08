package org.projects.centralpoint.middleware.Models.Factory;

import org.projects.centralpoint.File.Loader.Cell;
import org.projects.centralpoint.middleware.Models.*;
import org.projects.centralpoint.middleware.Models.Frontend.VideoFrontend;
import org.projects.centralpoint.Utils.String.StringHelper;
import org.projects.centralpoint.Extern.Fetching.FetchingStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.projects.centralpoint.Utils.DbData.DataHelper.getFormattedGenre;

public class ModelFactory
{
    public static ModelFactory GetInstance()
    {
        if(modelFactory == null)
        {
            modelFactory = new ModelFactory();
        }

        return modelFactory;
    }

    public Object ConvertModelToFrontend(Object model, String modelType) {
        if(modelType.equals("movie") || modelType.equals("tvSeries"))
        {
            Video video = (Video)model;
            VideoFrontend videoFrontend = new VideoFrontend();

            videoFrontend.setId(video.getId());
            videoFrontend.setTitle(video.getTitle());
            videoFrontend.setDescription(video.getDescription());
            videoFrontend.setVideoType(video.getVideoType());
            videoFrontend.setVideoStatus(video.getVideoStatus());
            videoFrontend.setBudget(video.getBudget());
            videoFrontend.setRevenue(video.getRevenue());
            videoFrontend.setSawIt(video.getSawIt());
            videoFrontend.setQuality(video.getQuality());
            videoFrontend.setPersonalScore(video.getPersonalScore());
            videoFrontend.setReleaseYear(video.getReleaseYear());
            videoFrontend.setReleaseDate(video.getReleaseDate());
            videoFrontend.setHasRomanianSub(video.getHasRomanianSub());
            videoFrontend.setHasEnglishSub(video.getHasEnglishSub());
            videoFrontend.setRuntime(video.getRuntime());
            videoFrontend.setAddedToDbDate(video.getAddedToDbDate());
            videoFrontend.setInternetScore(video.getInternetScore());
            videoFrontend.setInternetVotesNumber(video.getInternetVotesNumber());
            videoFrontend.setImdbID(video.getImdbID());
            videoFrontend.setImdbLink(video.getImdbLink());
            videoFrontend.setYoutubeLink(video.getYoutubeLink());
            videoFrontend.setStorageName(video.getStorageName());
            videoFrontend.setPoster(video.getTmdbPosterPath()); //This has been calculated outside of this method


            //TODO
            /*videoFrontend.setGenres();
            videoFrontend.setCountries();
            videoFrontend.setLanguages();
            videoFrontend.setActors();*/

            return videoFrontend;


        } else {
            return null;
        }
    }

    public Object CreateModel(String modelType, List<Cell> cells)
    {
        Object model = null;

        if(modelType.equals("movie") || modelType.equals("tvSeries"))
        {
            model = new Video();
            Video videoModelHandle = (Video)model;

            videoModelHandle.setVideoType(modelType);
            videoModelHandle.setFetchingStatus(FetchingStatus.WAITING.getIntValue());

            for(Cell cell : cells)
            {
                switch (cell.GetCellTitle())
                {
                    case "Title":
                    {
                        videoModelHandle.setTitle((String)cell.GetCellData());
                        break;
                    }
                    case "Const":
                    {
                        videoModelHandle.setImdbID((String)cell.GetCellData());
                        break;
                    }
                    case "URL":
                    {
                        videoModelHandle.setImdbLink((String)cell.GetCellData());
                        break;
                    }
                    case "IMDb Rating":
                    {
                        videoModelHandle.setInternetScore((BigDecimal) cell.GetCellData());
                        break;
                    }
                    case "Runtime (mins)":
                    {
                        videoModelHandle.setRuntime((int)cell.GetCellData());
                        break;
                    }
                    case "Year":
                    {
                        videoModelHandle.setReleaseYear((String)cell.GetCellData());
                        break;
                    }
                    case "Genres":
                    {
                        String genresList = (String)cell.GetCellData();

                        /* Trim the double quotes */
                        genresList = genresList.replaceAll("^\"|\"$", "");
                        List<String> genresArr = StringHelper.TokenizeByDelimiter(genresList, ",", false);
                        Set<Genre> genreList = new HashSet<>();

                        for(String genreStr : genresArr)
                        {
                            String genreToStore = getFormattedGenre(genreStr);
                            if(!genreToStore.isEmpty())
                            {
                                Genre genre = new Genre();
                                genre.setGenre(genreToStore);
                                genre.setVideo(videoModelHandle);
                                genreList.add(genre);
                            }
                        }

                        videoModelHandle.setGenres(genreList);
                        break;
                    }
                    case "Num Votes":
                    {
                        videoModelHandle.setInternetVotesNumber((int)cell.GetCellData());
                        break;
                    }
                    case "Release Date":
                    {
                        LocalDate releaseDate = null;
                        String stringReleaseDate = (String)cell.GetCellData();

                        try
                        {
                            if(stringReleaseDate != null)
                            {
                                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-d");
                                formatter = formatter.withLocale(Locale.UK);
                                releaseDate = LocalDate.parse(stringReleaseDate, formatter);
                            }
                        }
                        catch(Exception e)
                        {
                            releaseDate = null;
                        }

                        videoModelHandle.setReleaseDate(releaseDate);
                        break;
                    }
                    case "Directors":
                    {
                        //TODO to implement
                        break;
                    }
                    case "Storing location":
                    {
                        videoModelHandle.setStorageName((String)cell.GetCellData());
                        break;
                    }
                    default: continue;
                }
            }
        }

        return model;
    }

    private ModelFactory() {}

    private static ModelFactory modelFactory = null;
}

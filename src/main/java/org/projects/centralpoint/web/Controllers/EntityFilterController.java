package org.projects.centralpoint.web.Controllers;

import com.google.gson.Gson;
import org.projects.centralpoint.Defines.Types;
import org.projects.centralpoint.Defines.VideoGenres;
import org.projects.centralpoint.Filter.SearchFilter;
import org.projects.centralpoint.Utils.Resources.ResUtils;
import org.projects.centralpoint.middleware.Models.Factory.ModelFactory;
import org.projects.centralpoint.middleware.Models.Frontend.VideoFrontend;
import org.projects.centralpoint.middleware.Models.Frontend.VideoResponse;
import org.projects.centralpoint.middleware.Models.Video;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class EntityFilterController
{
    @Autowired
    org.projects.centralpoint.middleware.Services.VideoService videoService;

    @Autowired
    private ResUtils resUtils;

    @CrossOrigin(origins = "http://localhost:3100")
    @RequestMapping(value = "/video/search", method = RequestMethod.GET,
                    produces = MediaType.APPLICATION_JSON_VALUE)
    public String FilterGetHandler(ModelMap model)
    {
        Map<String, Object> dataForm = new HashMap<>();
        dataForm.put("genres", VideoGenres.getAllGenres());
        dataForm.put("countries", getWorldCountries());
        Gson gson = new Gson();
        return gson.toJson(dataForm);
    }

    @CrossOrigin(origins = "http://localhost:3100")
    @RequestMapping(value = "/video/search", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public String FilterPostHandler(@RequestBody Map<String, Object> jsonFilterMap)
    {
        VideoResponse response = new VideoResponse();
        response.setRequestType("filter");
        SearchFilter searchFilter = new SearchFilter(Types.EntityType.VIDEO);
        Gson gson = new Gson();

        String videoMainTitle = (String)jsonFilterMap.get("videoMainTitle");
        boolean videoExactTitle = (Boolean) jsonFilterMap.get("videoExactTitle");
        boolean videoSawIt = (Boolean) jsonFilterMap.get("videoSawIt");
        String videoReleaseYear = (String)jsonFilterMap.get("videoReleaseYear");
        boolean videoBeforeYear = (Boolean) jsonFilterMap.get("videoBeforeYear");
        boolean videoAfterYear = (Boolean) jsonFilterMap.get("videoAfterYear");
        String videoRuntime = (String)jsonFilterMap.get("videoRuntime");
        boolean videoWithinRuntime = (Boolean) jsonFilterMap.get("videoWithinRuntime");
        boolean videoExceedsRuntime = (Boolean) jsonFilterMap.get("videoExceedsRuntime");
        String videoInternetScore = (String)jsonFilterMap.get("videoInternetScore");
        boolean videoWithinInternetScore = (Boolean) jsonFilterMap.get("videoWithinInternetScore");
        boolean videoExceedsInternetScore = (Boolean) jsonFilterMap.get("videoExceedsInternetScore");
        ArrayList<String> videoGenres = (ArrayList<String>) jsonFilterMap.get("videoGenreMultiple");
        ArrayList<String> videoCountries = (ArrayList<String>) jsonFilterMap.get("videoCountryMultiple");

        if(videoMainTitle == null || videoMainTitle.isEmpty()) {
            response.setMsg("main title is missing");
            response.setStatus("fail");
            return gson.toJson(response);
        } else {
            searchFilter.AddFilter(SearchFilter.FilterType.FILTER_NAME, videoMainTitle);
        }

        if(videoExactTitle)
            searchFilter.TurnFilterSettingsON(SearchFilter.FilterSettings.FILTER_EXACT_TITLE);

        if(videoSawIt)
            searchFilter.AddFilter(SearchFilter.FilterType.FILTER_SAW_IT, true);

        if(videoReleaseYear != null && !videoReleaseYear.isEmpty()) {
            searchFilter.AddFilter(SearchFilter.FilterType.FILTER_YEAR, videoReleaseYear);

            if(videoBeforeYear) {
                searchFilter.TurnFilterSettingsON(SearchFilter.FilterSettings.FILTER_BEFORE_YEAR);
                searchFilter.TurnFilterSettingsOFF(SearchFilter.FilterSettings.FILTER_EXACT_YEAR);
            }
            else if(videoAfterYear) {
                searchFilter.TurnFilterSettingsON(SearchFilter.FilterSettings.FILTER_AFTER_YEAR);
                searchFilter.TurnFilterSettingsOFF(SearchFilter.FilterSettings.FILTER_EXACT_YEAR);
            }
        }

        if(videoRuntime != null && !videoRuntime.isEmpty()) {
            Integer runtime = Integer.parseInt(videoRuntime);
            searchFilter.AddFilter(SearchFilter.FilterType.FILTER_RUNTIME, runtime);

            if(videoWithinRuntime)
            {
                searchFilter.TurnFilterSettingsON(SearchFilter.FilterSettings.FILTER_WITHIN_RUNTIME);
                searchFilter.TurnFilterSettingsOFF(SearchFilter.FilterSettings.FILTER_EXACT_RUNTIME);
            }
            else if(videoExceedsRuntime)
            {
                searchFilter.TurnFilterSettingsON(SearchFilter.FilterSettings.FILTER_EXCEEDS_RUNTIME);
                searchFilter.TurnFilterSettingsOFF(SearchFilter.FilterSettings.FILTER_EXACT_RUNTIME);
            }
        }

        if(videoInternetScore != null && !videoInternetScore.isEmpty()) {
            Integer score = Integer.parseInt(videoInternetScore);
            searchFilter.AddFilter(SearchFilter.FilterType.FILTER_IMDB_SCORE, score);

            if(videoWithinInternetScore)
            {
                searchFilter.TurnFilterSettingsON(SearchFilter.FilterSettings.FILTER_WITHIN_IMDB_SCORE);
                searchFilter.TurnFilterSettingsOFF(SearchFilter.FilterSettings.FILTER_EXACT_IMDB_SCORE);
            }
            else if(videoExceedsInternetScore)
            {
                searchFilter.TurnFilterSettingsON(SearchFilter.FilterSettings.FILTER_EXCEEDS_IMDB_SCORE);
                searchFilter.TurnFilterSettingsOFF(SearchFilter.FilterSettings.FILTER_EXACT_IMDB_SCORE);
            }
        }

        //TODO check when using the filter if the genres and countries are used properly.
        // Because these are Lists and before we had arrays
        if(videoGenres != null && !videoGenres.isEmpty())
            searchFilter.AddFilter(SearchFilter.FilterType.FILTER_GENRE, videoGenres);

        if(videoCountries != null && !videoCountries.isEmpty())
            searchFilter.AddFilter(SearchFilter.FilterType.FILTER_COUNTRY, videoCountries);

        List<Video> videos = videoService.GetAllVideosByFilter(searchFilter, false);
        List<VideoFrontend> frontendVideos = new ArrayList<>();

        if(videos != null)
        {
            for (Video v : videos)
            {
                // We calculate the local poster path and set that into the Video model
                // so that we don't have to send it into ConvertModelToFrontend
                // as an extra parameter
                v.setTmdbPosterPath(resUtils.getVideoPosterPath(v));

                VideoFrontend fv = (VideoFrontend)ModelFactory.GetInstance().ConvertModelToFrontend(v, "movie");
                frontendVideos.add(fv);
            }
        }

        response.setVideos(frontendVideos);
        response.setStatus("success");
        return gson.toJson(response);
    }

    private String[] getWorldCountries()
    {
        String[] countries = new String[] {"Afghanistan", "Albania", "Algeria", "Andorra", "Angola", "Antigua and Barbuda",
                    "Argentina", "Armenia", "Australia", "Austria", "Azerbaijan", "Bahamas", "Bahrain",
                    "Bangladesh", "Barbados", "Belarus", "Belgium", "Belize", "Benin", "Bhutan", "Bolivia", "Bosnia and Herzegovina",
                    "Botswana", "Brazil", "Brunei", "Bulgaria", "Burkina Faso", "Burundi", "Côte d'Ivoire", "Cabo Verde", "Cambodia",
                    "Cameroon", "Canada", "Central African Republic", "Chad", "Chile", "China", "Colombia", "Comoros", "Congo",
                    "Costa Rica", "Croatia", "Cuba", "Cyprus", "Czech Republic", "Denmark", "Dominica", "Dominican Republic",
                    "Ecuador", "Egypt", "El Salvador", "Equatorial Guinea", "Eritrea", "Estonia", "Ethiopia", "Fiji",
                    "Finland", "France", "Gabon", "Gambia", "Georgia", "Germany", "Ghana", "Greece", "Grenada", "Guatemala",
                    "Guinea", "Guinea-Bissau", "Guyana", "Haiti", "Holy See", "Honduras", "Hungary", "Iceland", "India",
                    "Indonesia", "Iran", "Iraq", "Ireland", "Israel", "Italy", "Jamaica", "Japan", "Jordan", "Kazakhstan",
                    "Kenya", "Kiribati", "Kuwait", "Kyrgyzstan", "Laos", "Latvia", "Lebanon", "Lesotho", "Liberia", "Libya",
                    "Liechtenstein", "Lithuania", "Luxembourg", "Macedonia", "Madagascar", "Malawi", "Malaysia", "Maldives",
                    "Mali", "Malta", "Marshall Islands", "Mauritania", "Mauritius", "Mexico", "Micronesia", "Moldova", "Monaco",
                    "Mongolia", "Montenegro", "Morocco", "Mozambique", "Myanmar", "Namibia", "Nauru", "Nepal", "Netherlands",
                    "New Zealand", "Nicaragua", "Niger", "Nigeria", "North Korea", "Norway", "Oman", "Pakistan", "Palau",
                    "Palestine State", "Panama", "Papua New Guinea", "Paraguay", "Peru", "Philippines", "Poland", "Portugal",
                    "Qatar", "Romania", "Russia", "Rwanda", "Samoa", "San Marino", "Sao Tome and Principe", "Saudi Arabia",
                    "Senegal", "Serbia", "Seychelles", "Sierra Leone", "Singapore", "Slovakia", "Slovenia", "Solomon Islands",
                    "Somalia", "South Africa", "South Korea", "South Sudan", "Spain", "Sri Lanka", "Sudan", "Swaziland", "Sweden",
                    "Switzerland", "Syria", "Tajikistan", "Tanzania", "Thailand", "Togo", "Tonga", "Trinidad and Tobago", "Tunisia",
                    "Turkey", "Turkmenistan", "Tuvalu", "Uganda", "Ukraine", "United Arab Emirates", "United Kingdom",
                    "United States of America", "Uruguay", "Uzbekistan", "Vanuatu", "Venezuela", "Viet Nam", "Yemen", "Zambia", "Zimbabwe"};


        return countries;
    }
}

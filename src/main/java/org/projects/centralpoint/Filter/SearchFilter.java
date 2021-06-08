package org.projects.centralpoint.Filter;


import org.projects.centralpoint.Defines.Types;

import java.util.*;


public class SearchFilter
{
    public enum FilterType
    {
        // Video filters
        FILTER_NAME,
        FILTER_ACTORS,
        FILTER_GENRE,
        FILTER_COUNTRY,
        FILTER_SAW_IT,
        FILTER_YEAR,
        FILTER_PERSONAL_SCORE,
        FILTER_IMDB_SCORE,
        FILTER_RUNTIME,
        FILTER_TAGS,

        // App filters

        // Book filters

        // Picture filters

        // General filters
        FILTER_STORED_LOCATION
    }

    public class FilterSettings
    {
        public static final int FILTER_EXACT_TITLE              = 1 << 0;
        public static final int FILTER_EXACT_YEAR               = 1 << 1;
        public static final int FILTER_BEFORE_YEAR              = 1 << 2;
        public static final int FILTER_AFTER_YEAR               = 1 << 3;
        public static final int FILTER_EXACT_RUNTIME            = 1 << 4;
        public static final int FILTER_EXCEEDS_RUNTIME          = 1 << 5;
        public static final int FILTER_WITHIN_RUNTIME           = 1 << 6;
        public static final int FILTER_EXACT_PERSONAL_SCORE     = 1 << 7;
        public static final int FILTER_WITHIN_PERSONAL_SCORE    = 1 << 8;
        public static final int FILTER_EXCEEDS_PERSONAL_SCORE   = 1 << 9;
        public static final int FILTER_EXACT_IMDB_SCORE         = 1 << 10;
        public static final int FILTER_WITHIN_IMDB_SCORE        = 1 << 11;
        public static final int FILTER_EXCEEDS_IMDB_SCORE       = 1 << 12;
    }

    public SearchFilter(Types.EntityType searchEntityType)
    {
        m_searchEntityType = searchEntityType;
        m_searchFilterColl = new TreeMap<FilterType, Object>();

        // Apply the default filter settings
        TurnFilterSettingsOFF(FilterSettings.FILTER_EXACT_TITLE);
        TurnFilterSettingsON(FilterSettings.FILTER_EXACT_YEAR);
        TurnFilterSettingsON(FilterSettings.FILTER_EXACT_RUNTIME);
        TurnFilterSettingsON(FilterSettings.FILTER_EXACT_PERSONAL_SCORE);
        TurnFilterSettingsON(FilterSettings.FILTER_EXACT_IMDB_SCORE);
    }

    public void AddFilter(FilterType filterType, Object filterValue)
    {
        // If we already did had a value associated for this key,
        // then that value is overwritten
        m_searchFilterColl.put(filterType, filterValue);
        Reset();
    }

    public void RemoveFilter(FilterType filterType)
    {
        m_searchFilterColl.remove(filterType);
        Reset();
    }

    public void TurnFilterSettingsON(int filterSettingsToTurnON)
    {
        m_filterSettings |= filterSettingsToTurnON;
    }

    public void TurnFilterSettingsOFF(int filterSettingsToTurnOFF)
    {
        m_filterSettings &= ~filterSettingsToTurnOFF;
    }

    public boolean IsFilterSettingsON(int isFilterSettingsON)
    {
        return (m_filterSettings & isFilterSettingsON) > 0 ? true : false;
    }

    public int GetFilterCount()
    {
        return m_searchFilterColl.size();
    }

    public Object GetFilterValue(FilterType filterType)
    {
        return m_searchFilterColl.get(filterType);
    }

    public Types.EntityType GetSearchFilterType() { return m_searchEntityType; }

    public boolean GetNextFilter()
    {
        if(m_searchFilterCollIte != null && m_searchFilterCollIte.hasNext())
        {
            m_searchFilterPair = (Map.Entry)m_searchFilterCollIte.next();
            return true;
        }
        else
        {
            return false;
        }
    }

    public void Reset()
    {
        m_searchFilterCollIte = m_searchFilterColl.entrySet().iterator();
    }

    public FilterType GetCurrentFilterType() { return (FilterType) m_searchFilterPair.getKey(); }
    public Object GetCurrentFilterValue()
    {
        return (Object) m_searchFilterPair.getValue();
    }

    private Map<FilterType, Object> m_searchFilterColl      = null;
    private Iterator                m_searchFilterCollIte   = null;
    private Map.Entry               m_searchFilterPair      = null;
    private Types.EntityType        m_searchEntityType      = Types.EntityType.UNSPECIFIED;
    private int                     m_filterSettings        = 0;
}

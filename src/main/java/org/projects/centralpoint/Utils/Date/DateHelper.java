package org.projects.centralpoint.Utils.Date;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DateHelper
{
    public static LocalDate ConvertStringToLocalDate(String inDate)
    {
        LocalDate outDate;

        try
        {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            formatter = formatter.withLocale(Locale.UK);
            outDate = LocalDate.parse(inDate, formatter);
        }
        catch(Exception e)
        {
            outDate = null;
        }

        return outDate;
    }

    public static java.sql.Date ConvertLocalDateToSqlDate(String inDate)
    {
        java.sql.Date outDate = null;
        LocalDate date = ConvertStringToLocalDate(inDate);

        try
        {
            if(date != null)
                outDate = java.sql.Date.valueOf( date );
        }
        catch(Exception e)
        {
            outDate = null;
        }

        return outDate;
    }

    public static java.sql.Date ConvertStringToSqlDate(String inDate)
    {
        java.sql.Date outDate = null;

        try
        {
            if(inDate != null)
                outDate = java.sql.Date.valueOf( inDate );
        }
        catch(Exception e)
        {
            outDate = null;
        }

        return outDate;
    }
}

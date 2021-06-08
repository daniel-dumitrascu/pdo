package org.projects.centralpoint.Utils.Web;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;

public class WebJsonDld extends WebDld
{
    public WebJsonDld() { }

    public WebJsonDld(String link)
    {
        Load(link);
    }

    public void Load(String link)
    {
        // Before we can load a new link we
        // must reset the content of the object
        Reset();
        Connection.Response response = null;

        try
        {
            m_link = link;
            response = Jsoup.connect(m_link).ignoreContentType(true).execute();
            m_loaded = true;
        }
        catch (IOException ioe)
        {
            System.out.println("Could not load content for link: " + link);
            System.out.println("Possibly a bad connection to the internet.");
            return;
        }

        jsonBody = response.body();
        jsonHeader.setRateLimit(response.header("X-RateLimit-Limit"));
        jsonHeader.setRateLimitRemaining(response.header("X-RateLimit-Remaining"));
        jsonHeader.setRateLimitReset(response.header("X-RateLimit-Reset"));
    }

    public String GetPageBody()
    {
        return jsonBody;
    }

    public RequestHeaderDetails GetPageHeader()
    {
        return jsonHeader;
    }

    protected final void Reset()
    {
        m_loaded = false;
        m_link = "";
        jsonBody = "";
    }

    private String jsonBody;
    private RequestHeaderDetails jsonHeader = new RequestHeaderDetails();
}

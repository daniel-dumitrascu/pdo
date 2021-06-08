package org.projects.centralpoint.Utils.Web;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;


public class WebHtmlDld extends WebDld
{

    public WebHtmlDld()
    {

    }

    public WebHtmlDld(String link)
    {
        Load(link);
    }

    public void Load(String link)
    {
        // Before we can load a new link we
        // must reset the content of the object
        Reset();

        try
        {
            m_link = link;
            m_linkDoc = Jsoup.connect(m_link).get();
            m_loaded = true;
        }
        catch (IOException ioe)
        {
            ioe.printStackTrace();
        }
    }

    public String GetPageBody()
    {
        String htmlCode = new String();

        if(m_loaded)
            htmlCode = m_linkDoc.html();

        return htmlCode;
    }

    public RequestHeaderDetails GetPageHeader()
    {
        //TODO to implement
        return null;
    }

    protected final void Reset()
    {
        m_loaded = false;
        m_link = "";
    }

    private Document m_linkDoc;
}

package org.projects.centralpoint.Utils.Web;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class WebImageDlg extends WebDld
{
    public void Load(String link)
    {
        // Before we can load a new link we
        // must reset the content of the object
        Reset();

        try
        {
            URL url = new URL(link);
            inputStream = url.openStream();
            m_loaded = true;
        }
        catch (MalformedURLException e)
        {
            System.out.println("MalformedURLException :- " + e.getMessage());
        }
        catch (IOException e)
        {
            System.out.println("IOException :- " + e.getMessage());
        }
    }

    public Object GetPageBody()
    {
        return inputStream;
    }

    public RequestHeaderDetails GetPageHeader()
    {
        return null;
    }

    protected final void Reset()
    {
        if(inputStream != null)
        {
            try
            {
                inputStream.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

            inputStream = null;
        }
    }

    private InputStream inputStream = null;
}

package org.projects.centralpoint.Utils.Web;

public abstract class WebDld
{
    // Public functions
    public abstract void Load(String link);
    public abstract Object GetPageBody();
    public abstract RequestHeaderDetails GetPageHeader();
    public boolean IsLoaded() { return m_loaded; }

    // Protected functions
    protected abstract void Reset();

    // Members
    protected String m_link = new String();
    protected boolean m_loaded = false;
}

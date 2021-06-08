package org.projects.centralpoint.Settings;

import org.springframework.stereotype.Component;

@Component("appSettings")
public final class AppSettings
{
    public void setDbResourcesPath(String path)
    {
        dbResourcePath = path;
    }

    public String getDbResourcesPath()
    {
        return dbResourcePath;
    }

    private String dbResourcePath;
}
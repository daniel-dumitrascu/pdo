package org.projects.centralpoint.Extern;

import org.projects.centralpoint.Extern.Fetching.ExternFetcher;
import org.projects.centralpoint.Utils.Thread.ThreadHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;


@Component
public class ExternFetchingThread extends Thread
{
    @Override
    public void run()
    {
        addServicesToCollection();

        while(isBeanStillAlive)
        {
            Iterator it = entities.entrySet().iterator();
            while(it.hasNext())
            {
                Map.Entry pair = (Map.Entry)it.next();
                ExternFetcher fetcher = (ExternFetcher)pair.getValue();
                fetcher.fetch();
            }

            ThreadHelper.sleepCurrentThread(defaultSleepingTime);
        }
    }

    public void setBeenLifeStatus(boolean status)
    {
        isBeanStillAlive = status;
    }

    void notifyForWork(ExternDataEntity entity)
    {
        if(entities.containsKey(entity))
            entities.get(entity).notifyForWork();
    }

    private void addServicesToCollection()
    {
        entities = new TreeMap<>();
        entities.put(ExternDataEntity.VIDEO, videoExternFetcher);
        entities.put(ExternDataEntity.PEOPLE, peopleExternFetcher);
    }

    private Map<ExternDataEntity, ExternFetcher> entities;

    @Autowired
    @Qualifier("videoExternFetcher")
    private ExternFetcher videoExternFetcher;

    @Autowired
    @Qualifier("peopleExternFetcher")
    private ExternFetcher peopleExternFetcher;

    //TODO continue - implement the same for PeopleExternFetcher

    private boolean isBeanStillAlive = false;
    private Long defaultSleepingTime = 5000L;
}

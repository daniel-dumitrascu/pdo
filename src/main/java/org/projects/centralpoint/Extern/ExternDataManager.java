package org.projects.centralpoint.Extern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ExternDataManager implements InitializingBean, DisposableBean
{
    public void afterPropertiesSet()
    {
        externFetchingThread.setBeenLifeStatus(true);
        externFetchingThread.start();
    }

    public void destroy()
    {
        externFetchingThread.setBeenLifeStatus(false);

        try
        {
            externFetchingThread.join();
        }
        catch (InterruptedException e)
        {
            System.out.println("[ERROR] Can't join the fetching thread");
            logger.error("[ERROR] Can't join the fetching thread");
        }
    }

    public void workAvailableWarning(ExternDataEntity entity)
    {
        externFetchingThread.notifyForWork(entity);
    }

    @Autowired
    private ExternFetchingThread externFetchingThread;
    private Logger logger = LoggerFactory.getLogger(ExternDataManager.class);
}

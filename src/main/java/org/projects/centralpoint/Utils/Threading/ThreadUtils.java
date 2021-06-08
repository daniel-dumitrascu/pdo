package org.projects.centralpoint.Utils.Threading;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ThreadUtils
{
    /*
     * Sleep for the specified time amount on the current thread
     */
    public static void sleep(long time)
    {
        /*
         * Once we reach this block we will need to pause the execution on this thread
         * by the amount of time that we specified in time param
         */

        if(time > 0)
        {
            try
            {
                Thread.sleep(time);
            }
            catch (InterruptedException e)
            {
                logger.error("[ERROR] The waiting time in the worker thread was interrupted.");
                e.printStackTrace();
            }
        }
    }

    private static Logger logger = LoggerFactory.getLogger(ThreadUtils.class);
}

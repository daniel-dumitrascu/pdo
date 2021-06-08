package org.projects.centralpoint.Utils.Thread;

/**
 * Created by Daniel on 20-Oct-18.
 */
public class ThreadHelper
{
    public static void sleepCurrentThread(long timeToSleep)
    {
        /*
         * Once we reach this block we will need to pause the execution on the current thread
         * by the amount of time that we specified
         */

        try
        {
            Thread.sleep(timeToSleep);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }
}

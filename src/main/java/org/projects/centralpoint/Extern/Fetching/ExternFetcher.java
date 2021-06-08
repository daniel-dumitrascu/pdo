package org.projects.centralpoint.Extern.Fetching;

public interface ExternFetcher
{
    /*
     * This method does all the necessary work
     */
    void fetch();
    /*
     * This method is used to notify if there's work to be done
     */
    void notifyForWork();
}

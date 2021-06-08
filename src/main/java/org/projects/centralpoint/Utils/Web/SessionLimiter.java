package org.projects.centralpoint.Utils.Web;

/**
 * Created by Daniel on 11-Jun-18.
 */
public class SessionLimiter
{
    public SessionLimiter()
    {
        /*
         * Make an init of the session
         * with the default values
         */
        startSession(this.maxRequestPerBubble);
    }

    public SessionLimiter(int maxBubbleRequests)
    {
        startSession(maxBubbleRequests);
    }

    public void startSession(int maxBubbleRequests)
    {
        System.out.println("[Session Limiter] Session started, Requests " + this.maxRequestPerBubble);
        this.maxRequestPerBubble = maxBubbleRequests;
    }

    public void update(String remainingRequests, String futureResetTimestamp)
    {
        try
        {
            this.remainingRequests = Integer.parseInt(remainingRequests);
            this.futureResetTimestamp = Long.parseLong(futureResetTimestamp);
        }
        catch (NumberFormatException ex)
        {
            System.out.println("[Session Limiter] update error");
        }
    }

    public boolean canMakeRequests()
    {
        if((remainingRequests - reqChunk) >= 0)
        {
            System.out.println("[Session Limiter] YES remaining requests: " + remainingRequests);
            return true;
        }
        else
        {
            long currReqTime = System.currentTimeMillis() / 1000;

            if(currReqTime > futureResetTimestamp)
            {
                // It can happen than when current time to be after the future time to reset
                // In this case we manually reset the counter and then we return true
                remainingRequests = maxRequestPerBubble;

                System.out.println("[Session Limiter] YES timeframe was reset: " + remainingRequests);
                System.out.println("[Session Limiter] ---------------------------------------------------------------------------");

                return true;
            }
            else
            {
                // Calculate the remaining sleep time
                cooldownTime = futureResetTimestamp - currReqTime;

                System.out.println("[Session Limiter] NO cooldown time: " + cooldownTime);
                System.out.println("[Session Limiter] ---------------------------------------------------------------------------");

                return false;
            }
        }
    }

    public void setRequestsChunk(int req)
    {
        reqChunk = req;
    }

    public long getCooldownTime()
    {
        return cooldownTime;
    }

    private int maxRequestPerBubble = 40;       // The max request that can be done in the bubble time; default is 40
    private int reqChunk = 1;                   // The nr of requests you can make in each pass
    private long cooldownTime = 0;              // The remain time in to sleep till we can reset the counter
    private int remainingRequests = 0;          // Remaining requests than can be made
    private long futureResetTimestamp = 0;      // Timestamp at which counter is reset
}

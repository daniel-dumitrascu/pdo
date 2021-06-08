package org.projects.centralpoint.Utils.Web;

public class RequestHeaderDetails
{
    public void setRateLimit(String val) { rateLimit = val; }
    public String getRateLimit() { return rateLimit; }

    public void setRateLimitRemaining(String val) { rateLimitRemaining = val; }
    public String getRateLimitRemaining() { return rateLimitRemaining; }

    public void setRateLimitReset(String val) { rateLimitReset = val; }
    public String getRateLimitReset() { return rateLimitReset; }

    private String rateLimit;
    private String rateLimitRemaining;
    private String rateLimitReset;
}

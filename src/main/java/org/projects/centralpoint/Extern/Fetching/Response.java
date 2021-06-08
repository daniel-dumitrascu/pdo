package org.projects.centralpoint.Extern.Fetching;



public class Response
{
    public enum ResponseStatus
    {
        FETCH_OK,
        FETCH_FAIL,
        FETCH_LIMIT_EXCEEDED
    }

    public void setResponseStatus(ResponseStatus status) { this.status = status; }
    public ResponseStatus getResponseStatus() { return status; }

    //TODO are these 2 methods used?
    public void setCooldownTime(long time) { this.cooldownTime = time; }
    public long getCooldownTime() { return cooldownTime; }

    public void setData(Object data) { this.fetchedData = data; }
    public Object getData() { return fetchedData; }

    private ResponseStatus status = ResponseStatus.FETCH_FAIL;
    private long cooldownTime = 0;
    private Object fetchedData;
}

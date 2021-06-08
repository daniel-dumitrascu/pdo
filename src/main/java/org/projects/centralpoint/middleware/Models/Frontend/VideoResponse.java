package org.projects.centralpoint.middleware.Models.Frontend;

import java.util.List;

public class VideoResponse {

    public List<VideoFrontend> getVideos() { return videos; }
    public String getStatus() { return resStatus; }
    public String getMsg() { return resMsg; }
    public String getRequestType() { return requestType; }

    public void setVideos(List<VideoFrontend> videos) { this.videos = videos; }
    public void setStatus(String status) { this.resStatus = status; }
    public void setMsg(String msg) { this.resMsg = msg; }
    public void setRequestType(String requestType) { this.requestType = requestType; }

    private List<VideoFrontend> videos;
    private String resStatus;
    private String resMsg;
    private String requestType;
}

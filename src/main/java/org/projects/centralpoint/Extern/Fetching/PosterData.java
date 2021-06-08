package org.projects.centralpoint.Extern.Fetching;

import java.io.InputStream;

public class PosterData
{
    public PosterData(String posterName, int widthSize, int heightSize, String fileType, InputStream data)
    {
        this.posterName = posterName;
        this.widthSize = widthSize;
        this.heightSize = heightSize;
        this.fileType = fileType;
        this.data = data;
    }

    public String getPosterName() { return this.posterName; }
    public int getWidthSize() { return this.widthSize; }
    public int getHeightSize() { return this.heightSize; }
    public String getFileType() { return this.fileType; }
    public InputStream getData() { return data; }

    private String posterName;
    private String fileType;
    private int widthSize;
    private int heightSize;
    InputStream data;
}

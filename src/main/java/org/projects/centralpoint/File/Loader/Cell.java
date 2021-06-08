package org.projects.centralpoint.File.Loader;

public class Cell
{
    public Cell(String title, String type, Object data)
    {
        cellTitle = title;
        cellDataType = type;
        cellData = data;
    }

    public String GetCellTitle() { return cellTitle; }
    public String GetCellType() { return cellDataType; }
    public Object GetCellData() { return cellData; }

    private String cellTitle;
    private Object cellData;
    private String cellDataType;
}
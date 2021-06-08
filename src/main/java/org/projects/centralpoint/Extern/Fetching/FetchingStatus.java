package org.projects.centralpoint.Extern.Fetching;

public enum FetchingStatus
{
    WAITING(0),
    SUCCESS(1),
    FAIL(2);

    private int value;

    FetchingStatus(final int newValue)
    {
        if(newValue >= 0 && newValue <= 2)
            value = newValue;
    }

    public int getIntValue() { return value; }
}

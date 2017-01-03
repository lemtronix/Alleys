package controller;

import java.util.EventListener;

public interface MarbleModelListener extends EventListener 
{
    public void marbleUpdateEventOccurred(MarbleEvent me);
}

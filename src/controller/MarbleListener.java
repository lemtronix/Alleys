package controller;

import java.util.EventListener;

public interface MarbleListener extends EventListener 
{
    public void marbleEventOccurred(MarbleEvent me);
}

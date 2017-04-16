package view;

import java.util.EventListener;

public interface MarbleListener extends EventListener
{
    public void marbleClicked(SpotGraphic spotGraphic);
}

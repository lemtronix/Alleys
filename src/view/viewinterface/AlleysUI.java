package view.viewinterface;

import model.Card;
import model.Marble;
import model.Player;

public interface AlleysUI
{
    public void message(String s);
    public void message(String f, String ... pa);
    
    public void startTurn(Player player);
    
    public void setSpotMarble(int spotIndex, Marble marble);
    public void moveMarble(int fromIndex, int toIndex);
    public void swapMarbles(int firstIndex, int secondIndex);
    
    public void getMoveCount();
    
    public void chooseCard(Card card);
    public void unChooseCard();
}

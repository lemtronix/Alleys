package view.viewinterface;

import model.Card;
import model.Marble;
import model.Player;

public interface AlleysUI
{
    public void chooseCard(Card card);
    public int  getMoveCount();

    public void message(String s);
    public void message(String f, String ... pa);
    
    public void moveMarble(int fromIndex, int toIndex);
    public void setSpotMarble(int spotIndex, Marble marble);
    public void startTurn(Player player);
    public void swapMarbles(int firstIndex, int secondIndex);
    public void unChooseCard();
}

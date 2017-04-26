package view;

import javax.swing.Icon;
import javax.swing.JButton;

import model.Card;

public class CardButton extends JButton
{
    private CardGraphic cardGraphic;
    
    public void setCardGraphic(CardGraphic cg)
    {
        cardGraphic = cg;
        Icon icon = null;
        if (cardGraphic != null) { icon = cardGraphic.getIcon(); }
        setIcon(icon); // set the button's icon...
    }
    
    public CardGraphic getCardGraphic() { return cardGraphic; }

    public Card getCard()
    {   
        Card result = null;
        if (cardGraphic != null)
        {
            result = cardGraphic.getCard();
        }
        return result;
    }
    //    public ImageIcon   getCardIcon()    { return cardGraphic.getIcon(); }
}

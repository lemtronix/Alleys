package view;

import javax.swing.JButton;

import model.Card;

public class CardButton extends JButton
{
    private Card _Card;

    public void setCard(Card card)
    {
        _Card = card;
    }

    public Card getCard()
    {
        return _Card;
    }
}

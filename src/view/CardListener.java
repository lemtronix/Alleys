package view;

import model.Card;

public interface CardListener // extends EventListener
{
    public void cardPlayed(Card card);

    public void cardsFolded();
}

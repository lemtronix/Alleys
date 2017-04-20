package view;

import model.Card;

public interface CardListener // extends EventListener
{
    public void cardPlayed(CardButton source, Card card);

    public void cardsFolded();
}

package view;

import javax.swing.SwingUtilities;

import model.Deck;

public class Application
{
    public static void main(String[] args)
    {

        SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                new MainFrame();
                Deck _Deck = new Deck();

                while (_Deck.AnyCardsLeft())
                {
                    System.out.println(_Deck.GetRandomCard().toString());
                }

                System.out.println("Deck exhausted in " + _Deck.GetNumberOfAttemptsToClearDeck() + " attempts.");
            }
        });
    }
}

package view;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

import model.Card;

public class CardPanel extends JPanel
{
    private CardListener _CardListener = null;

    private ArrayList<CardButton> _CardButtons = new ArrayList<CardButton>(5);
    private JButton _FoldButton;

    public CardPanel()
    {
        _FoldButton = new JButton("Fold Cards");

        _FoldButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                if (_CardListener != null)
                {
                    _CardListener.cardsFolded();
                }
            }
        });

        createCardButtons();
        layoutComponents();
    }

    public void displayCards(List<Card> playersCards)
    {
        for (int i = 0; i < _CardButtons.size(); i++)
        {
            // Hide all the previous cards
            _CardButtons.get(i).setVisible(false);
        }

        for (int i = 0; i < playersCards.size(); i++)
        {
            // Display the new players cards
            Card card = playersCards.get(i);
            CardButton cardButton = _CardButtons.get(i);

            // CardGraphics should be flyweights
            CardGraphic cardGraphic = new CardGraphic(card);

            cardButton.setCard(card);
            cardButton.setIcon(cardGraphic.getImageIcon());
            cardButton.setToolTipText(card.toString());

            cardButton.setVisible(true);
        }
    }

    public void setCardListener(CardListener cardListener)
    {
        _CardListener = cardListener;
    }

    private void layoutComponents()
    {
        setBackground(Color.black);

        add(_CardButtons.get(0));
        add(_CardButtons.get(1));
        add(_CardButtons.get(2));
        add(_CardButtons.get(3));
        add(_CardButtons.get(4));

        add(_FoldButton);

        setPreferredSize(new Dimension(10, 100));
    }

    private void createCardButtons()
    {
        for (int i = 0; i < 5; i++)
        {
            CardButton cardButton = new CardButton();
            cardButton.setBackground(Color.black);
            cardButton.setPreferredSize(new Dimension(75, 100));

            cardButton.setBorder(BorderFactory.createEmptyBorder());
            cardButton.setMargin(new Insets(0, 0, 0, 0));

            cardButton.setFocusPainted(false);

            // Button will not indent when pressed
            cardButton.setContentAreaFilled(false);

            // Show a hand when over the button
            cardButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

            // cardButton.setBorderPainted(false);

            cardButton.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    if (_CardListener != null)
                    {
                        _CardListener.cardPlayed(cardButton, cardButton.getCard());

                        // cardButton.setVisible(false);
                    }
                }
            });

            _CardButtons.add(i, cardButton);
        }
    }

    public void hideCard(CardButton source)
    {
        source.setVisible(false);
    }
}

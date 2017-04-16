package view;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
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
        setBorder(BorderFactory.createLineBorder(Color.white));

        setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();

        gc.fill = GridBagConstraints.NONE;

        gc.insets = new Insets(0, 5, 0, 5);

        // Single Row
        gc.gridy = 0;

        gc.weightx = 0.0;
        gc.weighty = 0;

        gc.gridx = 0;
        add(_FoldButton, gc);

        gc.gridx++;
        add(_CardButtons.get(0), gc);

        gc.gridx++;
        add(_CardButtons.get(1), gc);

        gc.gridx++;
        add(_CardButtons.get(2), gc);

        gc.gridx++;
        add(_CardButtons.get(3), gc);

        gc.gridx++;
        add(_CardButtons.get(4), gc);

        setPreferredSize(new Dimension(10, 110));
    }

    private void createCardButtons()
    {
        for (int i = 0; i < 5; i++)
        {
            CardButton cardButton = new CardButton();
            cardButton.setBackground(new Color(0x2f, 0x4f, 0x4f));      // don't see this color...
            cardButton.setPreferredSize(new Dimension(75, 100));

            cardButton.setBorder(BorderFactory.createEmptyBorder());
            cardButton.setMargin(new Insets(0, 0, 0, 0));

            cardButton.setFocusPainted(false);

            // Button will not indent when pressed
            cardButton.setContentAreaFilled(false);

            // Show a hand when over the button
            cardButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

            cardButton.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    if (_CardListener != null)
                    {
                        _CardListener.cardPlayed(cardButton, cardButton.getCard());
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

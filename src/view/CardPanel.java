package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import model.Card;
import model.MarbleColor;

public class CardPanel extends JPanel
{
    private CardListener _CardListener = null;

    private ArrayList<CardButton> _CardButtons = new ArrayList<CardButton>(5);
    private JLabel nameLabel = new JLabel("", SwingConstants.CENTER);
    private JButton _FoldButton;
    private JPanel innerButtonPanel = null;
    

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
    
    private void layoutComponents()
    {
        setBackground(Color.black);
        setBorder(BorderFactory.createLineBorder(Color.white));
        
        innerButtonPanel = new JPanel();
        innerButtonPanel.setLayout(new GridLayout(4,1));
        nameLabel.setBorder(BorderFactory.createLineBorder(Color.black));
        nameLabel.setOpaque(true);
        innerButtonPanel.add(nameLabel);
        innerButtonPanel.add(_FoldButton);
        
        setLayout(new BorderLayout());
        add(innerButtonPanel, BorderLayout.WEST);
        
        JPanel cardPanel = new JPanel();
        BoxLayout cardPanelLayout = new BoxLayout(cardPanel, BoxLayout.LINE_AXIS);
        cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.LINE_AXIS));
        Dimension spacerDimension = new Dimension(5, 1);
        for (int i=0; i<5; i++) 
        { 
            cardPanel.add(Box.createRigidArea(spacerDimension)); 
            cardPanel.add(_CardButtons.get(i)); 
        }
        add(cardPanel, BorderLayout.CENTER);

//        setPreferredSize(new Dimension(10, 110));
    }

    public void setNameLabel(String name, MarbleColor marbleColor)
    {
        nameLabel.setText(name);
        nameLabel.setBackground(ViewColor.getSpotColor(marbleColor));
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

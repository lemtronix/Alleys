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
    private JPanel innerButtonPanel = null;     // holds nameLabel, fold button, anything else we think of.

    private JPanel cardCardPanel = null;

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
    
    public void addCard(CardGraphic cardGraphic)
    {
        CardButton emptyButton = null;
        for (CardButton cardButton : _CardButtons)
        {
            if (cardButton.getCardGraphic() == null)
            {
                emptyButton = cardButton;
                break;
            }
        }
        emptyButton.setCardGraphic(cardGraphic);
        emptyButton.setVisible(true);
        revalidate();
        repaint();
    }
    
    public void removeCard(Card card)
    {
        CardButton cardButtonForRemoval = null;
        for (CardButton cardButton : _CardButtons)
        {
            Card buttonCard = cardButton.getCard();
            if (buttonCard == card)
            {
                cardButtonForRemoval = cardButton;
                break;
            }
        }
        if (cardButtonForRemoval != null)
        {
//            _CardButtons.remove(cardButtonForRemoval);
            cardButtonForRemoval.setCardGraphic(null);
            cardButtonForRemoval.setVisible(false);
            revalidate();
            repaint();
        }
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
        
        cardCardPanel = new JPanel();
        GridLayout cardCardPanelLayout = new GridLayout(1, 5, 2, 0);
        cardCardPanel.setLayout(cardCardPanelLayout);
//        Dimension spacerDimension = new Dimension(5, 1);
        for (int i=0; i<5; i++) 
        { 
//            cardCardPanel.add(Box.createRigidArea(spacerDimension)); 
            cardCardPanel.add(_CardButtons.get(i)); 
        }
        add(cardCardPanel, BorderLayout.CENTER);

//        setPreferredSize(new Dimension(10, 110));
    }

    public void setNameLabel(String name, MarbleColor marbleColor)
    {
        nameLabel.setText(name);
        nameLabel.setBackground(ViewColor.getSpotColor(marbleColor));
    }

    public void displayCardList(List<Card> playersCards)
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
            CardGraphic cardGraphic = new CardGraphic(card);
            CardButton cardButton = _CardButtons.get(i);
            
            cardButton.setCardGraphic(cardGraphic);
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
        for (int i = 0; i < 5; i++) // there are 5 maximum cards always
        {
            CardButton cardButton = createCardButton();
            _CardButtons.add(cardButton);
        }
    }
    
    private CardButton createCardButton()
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
                    _CardListener.cardPlayed(cardButton.getCard());
                }
            }
        });
        return cardButton;
    }

}

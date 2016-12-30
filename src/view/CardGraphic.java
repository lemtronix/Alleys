package view;

import java.net.URL;

import javax.swing.ImageIcon;

import model.Card;

public class CardGraphic
{
    private ImageIcon _CardImageIcon = null;

    // TODO this should be the flyweight pattern
    // TODO also include a card back graphic
    public CardGraphic(Card card)
    {
        // Attempt to load the card's image
        _CardImageIcon = createImageIcon(card.getImagePath(), card.toString());
    }

    public ImageIcon getImageIcon()
    {
        return _CardImageIcon;
    }

    private ImageIcon createImageIcon(String path, String description)
    {
        URL imageUrl = getClass().getResource(path);

        if (imageUrl != null)
        {
            return new ImageIcon(imageUrl, description);
        }

        System.err.println("CardGraphic: Could not find the file in the specified path: " + path);
        return null;
    }
}
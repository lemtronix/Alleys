package view;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;

import model.Card;

public class CardGraphic
{
    private static void say(String msg) { System.out.println(msg); }
    private static String baseImageDir  = "images/";
    private static String suffix        = ".gif";
    
    private Card        card;
    private ImageIcon   icon;
    
    // static collection of card icons; we load this lazily as cards are displayed
    // we index this map with the 'index' of a card, a string representing value and suit.
    private static Map<String, ImageIcon> cardIcons = new HashMap<>();
    
    public CardGraphic(Card card)
    {
        this.card = card;
        icon = getCardGraphic(card);
    }
    
    public Card         getCard() { return card; }
    public ImageIcon    getIcon() { return icon; }
    
    private static ImageIcon getCardGraphic(Card card)
    {
        String cardIndex = card.getIndex();
        ImageIcon result = cardIcons.get(cardIndex);
        if (result == null)
        {
            // Load card image
            String imagePath = baseImageDir + card.getIndex() + suffix;
            result = createImageIcon(imagePath, card.toString());
            cardIcons.put(cardIndex, result);
        }
        return result;
    }
    
    private static ImageIcon createImageIcon(String path, String description)
    {
        ImageIcon result = null;
        URL imageUrl = CardGraphic.class.getResource(path);

        if (imageUrl == null)
        {
            System.err.println("CardGraphic: Could not find the file in the specified path: " + path);
        }
        else
        {
            result = new ImageIcon(imageUrl, description);
        }
        
        return result;
    }
}
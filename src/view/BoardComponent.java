package view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;

import model.AlleysGame;
import model.Board;
import model.Marble;
import model.MarbleColor;
import controller.Constants;

public class BoardComponent extends JComponent implements MarbleListener, MouseListener
{
    private static void say(String formatString, Object... args) { System.out.println(String.format(formatString, args)); }
    
    private AlleysGame  alleysGame;
    private JLabel      topPlayedCard   = null;   // card on top of the "stack" in the middle of the board
    
    // this array mirrors one in the model; this represents the spots on the board, while
    // that one represents the virtual spots in the actual game. The same indices apply to
    // each array.
    private SpotGraphic viewSpots[]     = new SpotGraphic[Constants.TOTAL_SPOTS];

    public BoardComponent(AlleysGame alleysGame)
    {
        this.alleysGame = alleysGame;

        setLayout(null);        // we use a null layout so we can put the spots at x,y positions.
        CreateSpots();
        addMouseListener(this);
    }
    
    /**
     * display the given card icon in the middle of the board panel.
     * If one is already there, remove it from the UI. If the given
     * icon is null, nothing is displayed, but a currently displayed
     * card is still removed.
     * @param icon
     */
    public void displayPlayedCard(Icon icon)
    {
        JLabel cardLabel = null;
        if (topPlayedCard != null)
        {
            remove(topPlayedCard);
            topPlayedCard = null;
        }
        
        if (icon != null)
        {
            cardLabel = new JLabel(icon);
            cardLabel.setBounds(245, 275, icon.getIconWidth(), icon.getIconHeight());
            add(cardLabel); // adds card to the panel
            topPlayedCard = cardLabel;
            repaint();
        }
        
    }
    
    public SpotGraphic getSpot(int index)
    {
        return viewSpots[index];
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        Graphics2D g2d = (Graphics2D) g;

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Background
        g2d.setColor(new Color(0x19, 0x19, 0x70)); // (Color.black); // 
        g2d.fillRect(0, 0, getWidth(), getHeight());

        UpdateSpots(g2d);
    }

    private void CreateSpots()
    {
        int INITIAL_SPOT_X = 65;        // these two constants define where the first spot will be put; the placement
        int INITIAL_SPOT_Y = 300;       // of the entire board on its panel is defined by this position. All other
                                        // spots are placed relative to this one, directly or indirectly.
        
        // create the 'loop' of spots around the board
        final int INLINE_SPACING = 20;      // spacing between spots that are in the same line, horizontal or vertical
        final int DIAGONAL_SPACING = 15;    // spacing (x and y) between spots that are in a diagonal line.
        int INLN = INLINE_SPACING;          // short versions of these constants make 
        int DIAG = DIAGONAL_SPACING;        // the array initializers easier to reads
        
        /*
         * This data structure is for drawing the loop around the board; for each three-int sub-array in the
         * larger array, the program draws one or more "spots" on the board after moving the indicated 
         * distance from the previous spot. The three ints in the sub-arrays are: 
         * 0: # of spots to draw, 
         * 1: x dist to move before each one, 
         * 2: y dist to move before each one.
         * This makes it easy to write a line of spots, changing up/down/diagonal with the 
         * direction and distance moved. 
         */
        int mainBoardInts[][] = {  
                                   { 1,    0,   0   }   // initial spot has no prior movement. It is the home spot
                                                        // of our red marbles, per our model definition (see the Board class)
                                  ,{ 1,    0, -INLN }   // One more marble next to Red Home, before we start in another direction
                                  ,{ 4, INLN,   0   }   // x changes), vertical (only y changes) or diagonal (x and y
                                  ,{ 6, DIAG, -DIAG }   // both change, positive or negative according to the direction 
                                  ,{ 4,    0, -INLN }   // of the slant. Each line segment is started in relation to the 
                                  ,{ 4, INLN,   0   }   // last spot of the previous segment. Positive values for y move
                                  ,{ 4,    0, INLN  }   // DOWN the page.
                                  ,{ 6, DIAG, DIAG  }
                                  ,{ 4, INLN,   0   }
                                  ,{ 4,    0, INLN  }
                                  ,{ 4,-INLN,   0   }
                                  ,{ 6,-DIAG, DIAG  }
                                  ,{ 4,    0, INLN  }
                                  ,{ 4,-INLN,   0   }
                                  ,{ 4,    0, -INLN }
                                  ,{ 6,-DIAG,-DIAG  }
                                  ,{ 4,-INLN,   0   }
                                  ,{ 2,    0, -INLN }
                                };
        
        int LastSpotX = INITIAL_SPOT_X;
        int LastSpotY = INITIAL_SPOT_Y;
        Color spotColor = Color.gray;
        
        int spotCounter = 0;            // spotCounter is the index into the overall Spots array;
                                        // we increment it just after adding each spot so it is ready for the next one.
        
        // draw main board loop
        // System.out.printf("%3s, %3s, %4s%n", "", "", "");
        for (int i=0; i<mainBoardInts.length; i++)
        {
          for (int j=0; j<mainBoardInts[i][0]; j++)
          {
            LastSpotX += mainBoardInts[i][1];
            LastSpotY += mainBoardInts[i][2];
            viewSpots[spotCounter] = new SpotGraphic(spotCounter, LastSpotX, LastSpotY, spotColor);
            // System.out.printf("%3d, %4d, %4d%n", spotCounter, LastSpotX, LastSpotY);
            spotCounter++;
          }
        }
        
        // the four "finishing rows"; first int is the index into the existing board array
        // for the spot next to the finishing row (i.e., the spot on the main
        // board's loop, with the four 'finishing' spots towards the center of the board
        // starting next to that spot). Then we have an index into a color array, and the 
        // indices of the x and y position deltas.
        int m = 8;
        int[][] finishingSpots = new int[][] { {71, 0,  INLN,     0,        0, m* INLN,  DIAG,  DIAG }
                                              ,{17, 1,     0,  INLN,  m*-INLN,       0, -DIAG,  DIAG }
                                              ,{35, 2, -INLN,     0,        0, m*-INLN, -DIAG, -DIAG }
                                              ,{53, 3,     0, -INLN,  m* INLN,       0,  DIAG, -DIAG }
                                             };

        // draw the four "finishing rows"; 
        for (int i=0; i<finishingSpots.length; i++) 
        {
          SpotGraphic startSpot = viewSpots[finishingSpots[i][0]];

          // set the color of the home spot -- it is one spot beyond the
          // spot at the start of the finishing row. This assumes that the
          // array holding the loop does not end/start right between one
          // of the finishing/home pairs.
          LastSpotX = startSpot.getXPosition();
          LastSpotY = startSpot.getYPosition();
          for (int j=0; j<4; j++)
          {
            LastSpotX += finishingSpots[i][2];
            LastSpotY += finishingSpots[i][3];
            viewSpots[spotCounter] = new SpotGraphic(spotCounter, LastSpotX, LastSpotY, spotColor);
            //            System.out.printf("%3d, %4d, %4d%n", spotCounter, LastSpotX, LastSpotY);
            spotCounter++;
          }
        }
        
        // now let's add in the diagonal lines for marbles at their starting positions
        for (int i=0; i<finishingSpots.length; i++)
        {
          int spotIndex = finishingSpots[i][0];
          SpotGraphic startSpot = viewSpots[spotIndex];
          LastSpotX = startSpot.getXPosition() + finishingSpots[i][4];
          LastSpotY = startSpot.getYPosition() + finishingSpots[i][5];
          Color color = spotColor; //          Color color = _marbleColors[finishingSpots[i][1]];
          for (int j=0; j<4; j++)
          {
            viewSpots[spotCounter] = new SpotGraphic(spotCounter, LastSpotX, LastSpotY, color);
//            System.out.printf("%3d, %4d, %4d%n", spotCounter, LastSpotX, LastSpotY);
            spotCounter++;
            LastSpotX += finishingSpots[i][6];
            LastSpotY += finishingSpots[i][7];
          }
        }
        
        // add colors to the spots that have them
        // NOTE: the model has 4 MarbleColor values, but they are not "colors" in any particular
        // UI library; here we use java.awt.Color, in another UI we might use something
        // else. Regardless of which UI color we use, the model always uses MarbleColor.
        Board board = alleysGame.getBoard();
        for (MarbleColor color : MarbleColor.values())
        {
            Color correspondingSpotColor = ViewColor.getSpotColor(color);

            int i = board.getStartingIndex(color);
            viewSpots[i].setBaseColor(correspondingSpotColor);
            
            i = board.getFirstStartingIndex(color);
            for (int j=0; j<4; j++)
            {
                viewSpots[i+j].setBaseColor(correspondingSpotColor);
            }
            
            i = board.getFirstFinishingIndex(color);
            for (int j=0; j<4; j++)
            {
                viewSpots[i+j].setBaseColor(correspondingSpotColor);
            }
        }
        
    }
    
    @Override public void marbleClicked(SpotGraphic spot)
    {
        System.out.println("marbleClicked: " + spot.getSpotIndex() + " " + spot.getMarble().getColor());
        alleysGame.marbleChosen(spot.getSpotIndex());
    }

    private void UpdateSpots(Graphics2D g2d)
    {
        for (int i = 0; i < viewSpots.length; i++)
        {
            g2d.setColor(viewSpots[i].getColor());
            g2d.fill(viewSpots[i].getShape());
        }
    }
    
    public void setSpotMarble(int spotIndex, Marble marble)
    {
        viewSpots[spotIndex].setMarble(marble);
    }

    // MouseListener methods
    // we trap each mouse click and see if it is on a spot that contains
    // a marble. If so, we report it with "mouseClicked()", otherwise we
    // just let it go.
    @Override    public void mouseClicked(MouseEvent e) 
    {        
        int x = e.getX();
        int y = e.getY();
        say("Mouse click at %d,%d", x, y);
        for (SpotGraphic spot : viewSpots)
        {
            if (spot.containsPosition(x,y) && spot.getMarble() != null)
            {
                say("Found spot %d", spot.getSpotIndex());
                marbleClicked(spot);
                break;
            }
        }
    }

    @Override    public void mousePressed(MouseEvent e)    {            }
    @Override    public void mouseReleased(MouseEvent e)    {         }
    @Override    public void mouseEntered(MouseEvent e)    {            }
    @Override    public void mouseExited(MouseEvent e)    {            }
    
    /// end of MouseListener methods

}

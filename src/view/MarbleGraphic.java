//package view;
//
//import java.awt.Color;
//import java.awt.event.MouseEvent;
//
//public class MarbleGraphic extends GamePieceGraphic
//{
//    private int _MarbleIdNumber = -1;
//    
//    public MarbleGraphic(int marbleIdNumber, int X, int Y, Color DesiredColor)
//    {
//        super(X, Y, DesiredColor);
//        _MarbleIdNumber = marbleIdNumber;
//    }
//
//    public void MoveTo(SpotGraphic spotToMoveTo)
//    {
//        if (spotToMoveTo == null)
//        {
//            System.err.println("Null spot received.");
//            return;
//        }
//
//        SetX(spotToMoveTo.GetX());
//        SetY(spotToMoveTo.GetY());
//    }
//
//    public int getMarbleIdNumber()
//    {
//        return _MarbleIdNumber;
//    }
//
//    protected void DraggedAction(MouseEvent e)
//    {
//        // SetX(e.getX());
//        // SetY(e.getY());
//    }
//}

package model;

public class MarbleStateFinish implements MarbleState
{

    @Override
    public MarbleState play(Marble marble, Card card)
    {
        // In the finish state, what needs to happen?
        return null;
    }

    @Override
    public void enter(Marble marble)
    {
        // Do we decrement the number of marbles in the finish state?
    }

    @Override
    public void exit(Marble marble)
    {

    }

    @Override
    public boolean isProtected()
    {
        return true;
    }

}

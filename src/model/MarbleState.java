package model;

public interface MarbleState
{
    public MarbleState play(Marble marble, Card card);

    public void enter(Marble marble);

    public void exit(Marble marble);

    public boolean isProtected();
}

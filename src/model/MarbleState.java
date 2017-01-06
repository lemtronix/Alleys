package model;

public interface MarbleState
{
    public MarbleState play(Marble marble, Card card, boolean splitSeven);

    public MarbleState playJack(Marble marble, Card card, Marble marbleToMoveTo);

    public void enter(Marble marble);

    public void exit(Marble marble);

    public boolean isProtected();
}

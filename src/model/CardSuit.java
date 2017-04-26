package model;

public enum CardSuit {
    Clubs       ("c"),
    Diamonds    ("d"),
    Hearts      ("h"),
    Spades      ("s");
    
    private String index;
    public String getIndex() { return index; }
    
    private CardSuit(String s) { index = s; }
}

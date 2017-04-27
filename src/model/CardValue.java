package model;

public enum CardValue {
    Ace     ("a", 1),
    Two     ("2", 2),
    Three   ("3", 3),
    Four    ("4", -4),
    Five    ("5", 5),
    Six     ("6", 6),
    Seven   ("7", 7),
    Eight   ("8", 8),
    Nine    ("9", 9),
    Ten     ("t", 10),
    Jack    ("j", -1),
    Queen   ("q", 11),
    King    ("k", 0);
    
    private String index;
    private int    value;
    public String getIndex() { return index; }
    public int    getValue() { return value; }
    
    private CardValue(String s, int v) { index = s; value = v; }
}

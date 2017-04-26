package model;

public enum CardValue {
    Ace     ("a"),
    Two     ("2"),
    Three   ("3"),
    Four    ("4"),
    Five    ("5"),
    Six     ("6"),
    Seven   ("7"),
    Eight   ("8"),
    Nine    ("9"),
    Ten     ("t"),
    Jack    ("j"),
    Queen   ("q"),
    King    ("k");
    
    private String index;
    public String getIndex() { return index; }
    
    private CardValue(String s) { index = s; }
}

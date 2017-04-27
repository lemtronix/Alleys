package model;

public interface Messager
{
    /**
     * output the given string to the message area on the UI.
     * @param s
     */
    public void message(String s);
    
    /**
     * Use the given format and parameters to create a string, and output that string
     * to the message area of the UI. The format string is defined in java.util.Formatter.
     * @param format
     * @param parameterValues
     * @see java.util.Formatter
     */
    public void message(String format, String ... parameterValues);
}

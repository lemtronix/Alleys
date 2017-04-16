package model;

public interface Messager
{
    public void message(String s);
    public void message(String format, String ... parameterValues);
}

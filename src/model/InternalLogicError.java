package model;

public class InternalLogicError extends Error
{
    public InternalLogicError(String msg) { super(msg); }
    public InternalLogicError(String msg, Exception e) { super(msg, e); }
}

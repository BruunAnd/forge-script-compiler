package com.forgescriptcompiler;

public class Command
{
    private boolean CanExecute = false;
    private boolean HasBeenExecuted = false;
    
    public synchronized void setHasBeenExecuted(boolean state)
    {
        HasBeenExecuted = state;
        this.notify();
    }
    
    public synchronized void setCanExecute(boolean state)
    {
        CanExecute = state;
        this.notify();
    }
    
    public synchronized void waitForCanExecute() throws InterruptedException
    {
        while (!CanExecute) wait();
    }
    
    public synchronized void waitForHasBeenExecuted()  throws InterruptedException
    {
        long now = System.currentTimeMillis();
        while (!HasBeenExecuted && (System.currentTimeMillis() - now) < 500) wait();
    }
}

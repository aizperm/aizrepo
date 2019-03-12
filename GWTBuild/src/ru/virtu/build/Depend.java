package ru.virtu.build;

public interface Depend
{
    boolean hasAnotherDepend();
    
    String getPath();
    
    String getDependPath();
    
}

package ru.virtu.build;

import java.io.File;

public class LibDepend implements Depend
{
    private File file;

    public LibDepend(File file)
    {
        super();
        this.file = file;
    }

    @Override
    public String getDependPath()
    {
        return file.getAbsolutePath();
    }

    @Override
    public boolean hasAnotherDepend()
    {
        return false;
    }

    @Override
    public String getPath()
    {
        return file.getAbsolutePath();
    }
    
    @Override
    public String toString()
    {        
        return getDependPath();
    }

}

package ru.virtu.build;

import java.io.File;

public class ProjectDepend implements Depend
{
    private File file;

    public ProjectDepend(File file)
    {
        super();
        this.file = file;
    }

    @Override
    public boolean hasAnotherDepend()
    {
        return true;
    }

    @Override
    public String getPath()
    {
        return file.getAbsolutePath();
    }

    @Override
    public String getDependPath()
    {
        return new File(file, "src").getAbsolutePath();
    }

    @Override
    public String toString()
    {        
        return getDependPath();
    }
}

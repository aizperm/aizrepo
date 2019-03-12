package ru.virtu.build;

import java.io.File;

public class ClasspathProjectPropertiesFactory implements IProjectPropertisFactory
{
    @Override
    public IProjectProperies create(File project)
    {
        return new ClasspathProjectsProperties(project);
    }
}

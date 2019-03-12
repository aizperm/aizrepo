package ru.virtu.build;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import electric.xml.Document;
import electric.xml.Element;
import electric.xml.Elements;
import electric.xml.ParseException;

public class ClasspathProjectsProperties implements IProjectProperies
{
    private File project;
    private File workspace;

    public ClasspathProjectsProperties(File project)
    {
        super();
        this.project = project;
        workspace = project.getParentFile();
    }

    @Override
    public List<Depend> getDepends()
    {
        List<Depend> deps = new ArrayList<>();        
        deps.add(new ProjectDepend(project));
        
        File classpath = new File(project, ".classpath");
        try
        {
            Document doc = new electric.xml.Document(classpath);
            Element root = doc.getRoot();
            Elements entries = root.getElements("classpathentry");
            while (entries.hasMoreElements())
            {
                Element classpathentry = entries.next();
                String kind = notNull(classpathentry.getAttributeValue("kind"));
                String path = notNull(classpathentry.getAttributeValue("path"));
                if (kind.equals("lib"))
                    deps.add(new LibDepend(new File(project, path)));
                else if (path.startsWith("/"))
                    deps.add(new ProjectDepend(new File(workspace, path)));
            }
        }
        catch (ParseException e)
        {
            throw new RuntimeException(e);
        }
        return deps;
    }

    private String notNull(String value)
    {
        return value != null ? value : "";
    }

    public static void main(String[] args)
    {

    }
}

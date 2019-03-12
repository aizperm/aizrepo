package ru.virtu.build;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ClassPathUtils
{
    private File rootProject;
    private File workspace;
    private IProjectPropertisFactory factory;
    private List<String> classpaths = new ArrayList<>();
    private Set<String> evalutedDeps = new HashSet<>();

    public ClassPathUtils(File rootProject, IProjectPropertisFactory factory)
    {
        super();
        this.rootProject = rootProject;
        this.factory = factory;
        workspace = rootProject.getParentFile();
    }

    public List<String> collect()
    {
        File project = rootProject;
        try
        {
            collect(project);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return classpaths;
    }

    private void collect(File project)
    {
        IProjectProperies projectProperties = factory.create(project);
        List<Depend> depends = projectProperties.getDepends();
        depends.forEach((dep) -> {
            if (!dep.hasAnotherDepend())
                classpaths.add(dep.getDependPath());
            else
            {
                if (!evalutedDeps.contains(dep.getPath()))
                {
                    evalutedDeps.add(dep.getPath());
                    classpaths.add(dep.getPath());
                    classpaths.add(dep.getDependPath());
                    collect(new File(dep.getPath()));
                }
            }
        });
    }
}

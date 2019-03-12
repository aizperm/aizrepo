package ru.virtu.build;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import electric.xml.Document;
import electric.xml.Element;
import electric.xml.Elements;

public class BuildGWT
{
    private String projectName;
    private String entryPoint;
    private String webRoot;
    private List<String> addedClasspath = new ArrayList<>();
    private String agent;

    public BuildGWT(String projectName, String entryPoint, String webRoot, String agent)
    {
        super();
        this.projectName = projectName;
        this.entryPoint = entryPoint;
        this.webRoot = webRoot;
        this.agent = agent;
    }

    public void build()
    {
        File workspace = getWorkspaceDir();
        File gwtProject = new File(workspace, projectName);
        ClassPathUtils pathUtils = new ClassPathUtils(gwtProject, new ClasspathProjectPropertiesFactory());
        List<String> collect = pathUtils.collect();
        collect.add(new File(gwtProject, "src").getAbsolutePath());

        File entryPointDir = getEntryPointDir(gwtProject);
        File gwtXmlFile = new File(entryPointDir, getEntryPointName() + ".gwt.xml");
        File tempFile = getTempFile();
        copy(gwtXmlFile, tempFile);
        System.out.println("Copy to " + tempFile.getAbsolutePath());
        try
        {
            addAgent(gwtXmlFile);
            run(collect);
        }
        finally
        {
            copy(tempFile, gwtXmlFile);
        }

    }

    private void run(List<String> collect)
    {
        try
        {
            collect.addAll(addedClasspath);
            for (String project : addedClasspath)
            {
                ClassPathUtils pathUtils = new ClassPathUtils(new File(project), new ClasspathProjectPropertiesFactory());
                List<String> collected = pathUtils.collect();
                collect.addAll(collected);
            }
            String classpath = toStringClasspath(collect);

            ProcessBuilder builder = new ProcessBuilder();

            String javahome = System.getProperty("java.home");

            builder.command(javahome + "\\bin\\java.exe",
                    "-cp",
                    classpath,
                    "-Xmx4048M",
                    "com.google.gwt.dev.Compiler",
                    "-war",
                    "\"" + webRoot + "\"",
                    "-logLevel",
                    "WARN",
                    "-localWorkers",
                    "2",
                    "-logLevel",
                    "WARN",
                    entryPoint);

            File workspace = getWorkspaceDir();
            builder.directory(workspace);

            long start = System.currentTimeMillis();
            System.out.println("Build started");
            builder.redirectErrorStream(true);
            builder.inheritIO();

            Process process = builder.start();

            int exitVal = process.waitFor();
            System.out.printf("Exit %s\n", exitVal);
            long end = System.currentTimeMillis();
            System.out.println("Total time: " + ((end - start) / 1000) + " sec.");
            System.exit(0);

        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    private String toStringClasspath(List<String> collect)
    {
        StringBuilder b = new StringBuilder();
        collect.forEach(path -> b.append(path).append(";"));
        return "\"" + b.toString() + "\"";
    }

    private void addAgent(File gwtXmlFile)
    {
        try
        {
            Document doc = new Document(gwtXmlFile);
            Element root = doc.getRoot();
            boolean has = false;
            Elements elements = root.getElements("set-property");
            while (elements.hasMoreElements())
            {
                Element next = elements.next();
                String attributeValue = next.getAttributeValue("name");
                if ("user.agent".equals(attributeValue))
                {
                    has = true;
                    break;
                }
            }
            if (!has)
            {
                Element element = root.addElement("set-property");
                element.setAttribute("name", "user.agent");
                element.setAttribute("value", agent);
                doc.write(gwtXmlFile);
            }
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    private void copy(File gwtXmlFile, File tempFile)
    {
        try
        {
            Files.copy(gwtXmlFile.toPath(), tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private File getTempFile()
    {
        try
        {
            File createTempFile = File.createTempFile("gwt", "xml");
            createTempFile.deleteOnExit();
            return createTempFile;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private File getEntryPointDir(File gwtProject)
    {
        StringBuilder b = new StringBuilder();
        String[] split = entryPoint.split("[.]");
        for (int i = 0; i < split.length - 1; i++)
        {
            if (b.length() > 0)
                b.append("/");
            b.append(split[i]);
        }

        return new File(gwtProject, "src/" + b.toString());
    }

    private String getEntryPointName()
    {
        String[] split = entryPoint.split("[.]");
        return split[split.length - 1];
    }

    private File getWorkspaceDir()
    {
        return new File(new File("").getAbsolutePath()).getParentFile();
    }

    public void addClasspath(String path)
    {
        addedClasspath.add(path);
    }
}

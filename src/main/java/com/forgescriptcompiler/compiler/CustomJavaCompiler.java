package com.forgescriptcompiler.compiler;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;

import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;

public class CustomJavaCompiler
{    
    /* Some JDKs need the class path in order to compile the script correctly */
    private String getClassPath()
    { 
        StringBuffer stringBuffer = new StringBuffer();
        for (URL url : ((URLClassLoader) (Thread.currentThread().getContextClassLoader())).getURLs())
            stringBuffer.append(new File(url.getPath()) + System.getProperty("path.separator"));
        
        // Remove last path separator
        String classPath = stringBuffer.toString();
        return classPath.substring(0, classPath.lastIndexOf(System.getProperty("path.separator")));
    } 
    
    public Class<?> compile(String scriptsLocation, String scriptName) throws Exception
    {
        JavaCompiler javaCompiler = ToolProvider.getSystemJavaCompiler();
        if (javaCompiler == null)
            throw new Exception("A Java compiler was not found. Please run this modification with JDK."); // TODO custom exception
        
        StandardJavaFileManager fileManager = javaCompiler.getStandardFileManager(null, null, null);

        fileManager.setLocation(StandardLocation.CLASS_OUTPUT, Arrays.asList(new File(scriptsLocation)));

        File[] javaFiles = new File[] { new File(scriptsLocation + scriptName + ".java")};
        if (!javaFiles[0].exists())
            throw new FileNotFoundException("Script file " + javaFiles[0] + " not found!");
        
        ArrayList<String> optionList = new ArrayList<String>(); 
        optionList.add("-classpath");
        optionList.add(getClassPath());
        
        CompilationTask compilationTask = javaCompiler.getTask(null, null, null, optionList, null, fileManager.getJavaFileObjects(javaFiles));
        if (!compilationTask.call())
            throw new Exception("Could not compile script!"); // TODO custom exception

        CustomClassLoader loader = new CustomClassLoader(CustomClassLoader.class.getClassLoader());
        return loader.loadClassFromFile(scriptName, new File(scriptsLocation + scriptName + ".class"));
    }
}

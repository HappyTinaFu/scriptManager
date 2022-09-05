package com.tinafu;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CustomJavaExcuter {
    static ScriptExcuterManager excuterManager = new ScriptExcuterManager();

    public ExcuterResult compiler_class(String java_file_path,String external_jar_Path,String class_save_dir) {
        ArrayList<String> args = new ArrayList<>();
        args.add("-cp");
        args.add(".:"+external_jar_Path+"*");
        args.add(java_file_path);
        args.add("-d");
        args.add(class_save_dir);
        try {
            return excuterManager.scriptExcuter("javac",args);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ExcuterResult(-1,"compiler fail");
    }

    public ExcuterResult excute_class(String external_jar_Path,String class_save_dir,String class_name,String[] excute_args) {
        ArrayList<String> args = new ArrayList<>();
        args.add("-Djava.ext.dirs=" + external_jar_Path);
        args.add("-classpath");
        args.add(class_save_dir);
        args.add(class_name);
        for (String arg : excute_args) {
            args.add(arg);
        }
        try {
            return excuterManager.scriptExcuter("java",args);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ExcuterResult(-1,"excute class fail");
    }

    /**
     * 获取类的全名称
     */
    public String getFullClassName(String sourceCode) {
        String className = "";
        Pattern pattern = Pattern.compile("package\\s+\\S+\\s*;");
        Matcher matcher = pattern.matcher(sourceCode);
        if (matcher.find()) {
            className = matcher.group().replaceFirst("package", "").replace(";", "").trim() + ".";
        }

        pattern = Pattern.compile("class\\s+\\S+\\s+\\{");
        matcher = pattern.matcher(sourceCode);
        if (matcher.find()) {
            className += matcher.group().replaceFirst("class", "").replace("{", "").trim();
        }
        return className;
    }

    public static void main( String[] args ){
        String java_file_path = "/Users/fuwenlan/Desktop/dev/workspaces/java/scriptManager/src/main/java/com/tinafu/App.java";
        String custom_class_base_dir = "/Users/fuwenlan/Desktop/";
        String external_jar_Path = "/Users/fuwenlan/Desktop/dev/scriptManager-jar/BOOT-INF/lib/";

        CustomJavaExcuter javaExcuter = new CustomJavaExcuter();
        //System.out.println(javaExcuter.compiler_class(java_file_path,external_jar_Path,custom_class_base_dir));
        //System.out.println(javaExcuter.excute_class(external_jar_Path,custom_class_base_dir,"com.tinafu.App", new String[]{"this", "is", "demo"}));

        System.out.println(javaExcuter.getFullClassName("" +
                "\n" +
                "import javax.script.*;\n" +
                "\n" +
                "import java.io.File;\n" +
                "import java.io.FileNotFoundException;\n" +
                "import java.io.FileReader;\n" +
                "import java.io.StringWriter;\n" +
                "\n" +
                "import org.apache.commons.exec.CommandLine;\n" +
                "\n" +
                "/**\n" +
                " * Hello world!\n" +
                " *\n" +
                " */\n" +
                "public class App \n" +
                "{\n" +
                "    public static void main( String[] args )\n" +
                "    {"));
    }

}

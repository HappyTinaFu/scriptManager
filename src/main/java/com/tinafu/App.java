package com.tinafu;
import org.apache.commons.exec.CommandLine;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        for(int i=0;i<args.length;i++) {
            System.out.println("args[" + i + "]" + args[i]);
        }

        CommandLine cmdLine = new CommandLine("python");
        cmdLine.addArgument("abc",false);

//        StringWriter output = new StringWriter();
//        ScriptContext context = new SimpleScriptContext();
//        context.setWriter(output);
//
//        ScriptEngineManager manager = new ScriptEngineManager();
//        ScriptEngine engine = manager.getEngineByName("python");
//        try {
//            engine.eval(new FileReader("hello.py"), context);
//        } catch (ScriptException e) {
//            e.printStackTrace();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//        System.out.println(output.toString());
    }
}

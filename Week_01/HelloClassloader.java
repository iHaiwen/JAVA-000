package com.geekuniversity.javapratice.week01;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;

/**
 * @author ihaiwen
 * @date 2020/10/20
 * <p>
 * 描述自定义class loader
 */
public class HelloClassloader extends ClassLoader {

    private static final String FILE_PATH = "xxx/Hello.xlass";

    public static void main(String[] args) {
        try {
            Class aClass = new HelloClassloader().findClass("Hello");
            Method helloMethod = aClass.getDeclaredMethod("hello");
            helloMethod.setAccessible(true);
            helloMethod.invoke(aClass.newInstance());
        } catch (ClassNotFoundException | ClassFormatError e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException, ClassFormatError {
        byte[] bytes = getBytesFromFile();
        bytes = decode(bytes);
        return defineClass(name, bytes, 0, bytes.length);
    }

    private byte[] getBytesFromFile() {
        File file = new File(FILE_PATH);
        if (!file.exists() || !file.isFile()) {
            return new byte[0];
        }

        try {
            return Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            e.printStackTrace();
            return new byte[0];
        }
    }

    private byte[] decode(byte[] bytes) {
        int length = bytes.length;
        if (length == 0) {
            return bytes;
        }

        for (int i = 0; i < length; i++) {
            bytes[i] = (byte) (255 - bytes[i]);
        }
        return bytes;
    }
}

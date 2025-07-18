package space.commandf1.cracker.lpx.util;

import org.objectweb.asm.*;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ClassUtil {
    private static final Object CLASS_LOADING_LOCK = new Object();

    @SuppressWarnings("SameParameterValue")
    public static byte[] modifyClass(byte[] originalBytes,
                                     String methodName,
                                     String parmsType,
                                     String classPath,
                                     String fieldName,
                                     String fieldType,
                                     String targetOwner,
                                     String targetName,
                                     String targetDescriptor) {
        ClassReader cr = new ClassReader(originalBytes);
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        ClassVisitor cv = new ClassVisitor(Opcodes.ASM5, cw) {
            @Override
            public MethodVisitor visitMethod(
                    int access, String name, String desc, String signature, String[] exceptions
            ) {
                if (Arrays.equals(methodName.getBytes(), name.getBytes()) && Arrays.equals(parmsType.getBytes(), desc.getBytes())) {
                    return new MethodVisitor(Opcodes.ASM5, super.visitMethod(access, name, desc, signature, exceptions)) {
                        @Override
                        public void visitCode() {
                            mv.visitVarInsn(Opcodes.ALOAD, 0);
                            mv.visitFieldInsn(Opcodes.GETFIELD, classPath, fieldName, fieldType);
                            mv.visitMethodInsn(
                                    Opcodes.INVOKESTATIC,
                                    targetOwner,
                                    targetName,
                                    targetDescriptor,
                                    false
                            );
                            mv.visitInsn(Opcodes.ICONST_1);
                            mv.visitInsn(Opcodes.IRETURN);
                        }
                    };
                }
                return super.visitMethod(access, name, desc, signature, exceptions);
            }
        };
        cr.accept(cv, 0);
        return cw.toByteArray();
    }

    public static boolean isClassLoaded(String className, ClassLoader classLoader) {
        try {
            Method findLoadedMethod = ClassLoader.class.getDeclaredMethod("findLoadedClass", String.class);
            findLoadedMethod.setAccessible(true);
            return findLoadedMethod.invoke(classLoader, className) != null;
        } catch (Exception e) {
            return false;
        }
    }

    @SuppressWarnings("unchecked")
    public static void unloadClass(String className, ClassLoader classLoader) {
        try {
            Method findLoadedMethod = ClassLoader.class.getDeclaredMethod("findLoadedClass", String.class);
            findLoadedMethod.setAccessible(true);
            Class<?> loadedClass = (Class<?>) findLoadedMethod.invoke(classLoader, className);

            if (loadedClass != null) {
                try {
                    Field classesField = classLoader.getClass().getDeclaredField("classes");
                    classesField.setAccessible(true);
                    Map<String, Class<?>> classes = (Map<String, Class<?>>) classesField.get(classLoader);
                    classes.remove(className);
                } catch (NoSuchFieldException ignored) {
                }

                Field classLoaderField = Class.class.getDeclaredField("classLoader");
                classLoaderField.setAccessible(true);
                classLoaderField.set(loadedClass, null);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to unload class: " + className, e);
        }
    }

    public static byte[] getClassBytes(String path, Class<?> clazz) throws Exception {
        URL jarUrl = clazz.getProtectionDomain().getCodeSource().getLocation();
        try {
            File jarFile = new File(jarUrl.toURI());
            try (JarFile jar = new JarFile(jarFile)) {
                JarEntry entry = jar.getJarEntry(path);
                if (entry != null) {
                    try (InputStream is = jar.getInputStream(entry)) {
                        return StreamUtil.readAllBytesManual(is);
                    }
                }
            }
        } catch (URISyntaxException e) {
            throw new RuntimeException("Failed to convert URL to URI: " + jarUrl, e);
        }
        return null;
    }

    public static void defineClassWithReflection(ClassLoader loader, String className, byte[] b) {
        synchronized (CLASS_LOADING_LOCK) {
            try {
                unloadClass(className, loader);

                if (isClassLoaded(className, loader)) {
                    throw new IllegalStateException("Class still loaded after unloading: " + className);
                }

                Method defineMethod = ClassLoader.class.getDeclaredMethod(
                        "defineClass", String.class, byte[].class, int.class, int.class
                );
                defineMethod.setAccessible(true);
                defineMethod.invoke(loader, className, b, 0, b.length);
            } catch (Exception e) {
                throw new RuntimeException("Failed to define class: " + className, e);
            }
        }
    }
}
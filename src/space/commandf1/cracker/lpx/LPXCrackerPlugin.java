package space.commandf1.cracker.lpx;

import it.ytnoos.lpx.I2;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.objectweb.asm.*;
import space.commandf1.cracker.lpx.util.ClassUtil;

import java.util.Arrays;

public class LPXCrackerPlugin extends I2 {
    private static LPXCrackerPlugin instance;

    public LPXCrackerPlugin() {
        super();
        instance = this;
    }

    public static LPXCrackerPlugin getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        this.getLogger().info("LPX v3.6.7 was cracked by commandf1");
        this.getLogger().info("ENJOY!");
        try {
            modify(this.getClassLoader());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        super.onEnable();

        // Now let's register our own command.
        PluginCommand lpxCommand = this.getCommand("lpx");
        CommandExecutor executor = lpxCommand.getExecutor();
        lpxCommand.setTabCompleter(lpxCommand.getTabCompleter());
        lpxCommand.setExecutor((commandSender, command, s, strings) -> {
            if (strings.length == 0) {
                commandSender.sendMessage("This server is running a NULLED version of LPX v3.6.7");
                commandSender.sendMessage("Cracked By commandf1 (QQ: 985248257 | Discord: commandf1)");
                commandSender.sendMessage("Download LPX cracked: https://github.com/commandf1/LPXCracker.git");
            }
            return executor.onCommand(commandSender, command, s, strings);
        });
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    @Override
    public void onLoad() {
        super.onLoad();
    }

    private static void modify(ClassLoader classLoader) throws Exception {
        if (ClassUtil.isClassLoaded("it.ytnoos.lpx.z", classLoader)) {
            ClassUtil.unloadClass("it.ytnoos.lpx.z", classLoader);
        }

        byte[] classBytes = ClassUtil.getClassBytes("it/ytnoos/lpx/z.class", LPXCrackerPlugin.class);
        if (classBytes == null) {
            throw new RuntimeException("Class not found!");
        }

        byte[] bytes = modifyClass(classBytes,
                "Ò²Ð…",
                "([Ljava/lang/Object;)Z",
                "it/ytnoos/lpx/z",
                "Ï ",
                "Lit/ytnoos/lpx/api/LPX;",
                "space/commandf1/cracker/lpx/util/CrackerUtil",
                "executeFinishVerification",
                "(Ljava/lang/Object;)V");
        ClassUtil.defineClassWithReflection(classLoader, "it.ytnoos.lpx.z", bytes);
    }

    @SuppressWarnings("SameParameterValue")
    private static byte[] modifyClass(byte[] originalBytes,
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
}
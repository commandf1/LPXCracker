package space.commandf1.cracker.lpx.util;

import it.ytnoos.lpx.api.LPX;
import space.commandf1.cracker.lpx.LPXCrackerPlugin;

import java.lang.reflect.Method;

/**
 * Don't delete this class, you idiot.
 * */
@SuppressWarnings("unused")
public class CrackerUtil {

    /**
     * IDK why but the author likes to add another verification for the plugin
     * Should be executed by method Ò²Ð… at it.ytnoos.lpx.z
     * <p>
     * CODE SHOULD BE:
     * DEFINE PRIVATE Ò²Ð…([Ljava/lang/Object; a1)Z
     * A:
     * ALOAD this
     * GETFIELD it/ytnoos/lpx/z.Ï  Lit/ytnoos/lpx/api/LPX;
     * INVOKESTATIC space/commandf1/cracker/lpx/util/CrackerUtil.executeFinishVerification(Ljava.lang.Object;)V
     * ICONST_1
     * IRETURN
     * B:
     * */
    @SuppressWarnings("JavaReflectionMemberAccess")
    public static void executeFinishVerification(Object lpx) {
        if (!(lpx instanceof LPX)) {
            return;
        }

        try {
            Class<?> aClass = Class.forName("it.ytnoos.lpx.q");
            Object o = aClass.newInstance();
            Method declaredMethod = aClass.getDeclaredMethod("q", lpx.getClass());
            declaredMethod.setAccessible(true);
            declaredMethod.invoke(o, lpx);
            LPXCrackerPlugin.getInstance().getLogger().info("Successfully hooked into Finish Verification method.");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

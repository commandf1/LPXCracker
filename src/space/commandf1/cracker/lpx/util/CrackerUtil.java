package space.commandf1.cracker.lpx.util;

import it.ytnoos.lpx.api.LPX;
import space.commandf1.cracker.lpx.LPXCrackerPlugin;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Don't delete this class, you idiot.
 *
 * @author commandf1
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
    // @SuppressWarnings("JavaReflectionMemberAccess")
    @SuppressWarnings("JavaReflectionMemberAccess")
    public static void executeFinishVerification(Object lpx) {
        if (!(lpx instanceof LPX)) {
            return;
        }

        try {
            Class<?> afterVerificationSetupClass = Class.forName("it.ytnoos.lpx.q");
            MethodHandles.Lookup lookup = MethodHandles.lookup();
            Object afterVerificationSetupClassObject = lookup
                    .findConstructor(afterVerificationSetupClass, MethodType.methodType(void.class))
                    .invoke();
            Method afterVerificationSetupClassMethod = afterVerificationSetupClass.getDeclaredMethod("q", lpx.getClass());
            afterVerificationSetupClassMethod.setAccessible(true);
            lookup.unreflect(afterVerificationSetupClassMethod)
                    .invoke(afterVerificationSetupClassObject, lpx);
            LPXCrackerPlugin.getInstance().getLogger().info("Successfully hooked into Finish Verification method.");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Required class not found: it.ytnoos.lpx.q", e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Required method 'q' not found in class it.ytnoos.lpx.q", e);
        } catch (InstantiationException e) {
            throw new RuntimeException("Failed to instantiate class it.ytnoos.lpx.q", e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Access denied when invoking method", e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException("Exception occurred during method invocation", e.getCause());
        } catch (Throwable e) {
            throw new RuntimeException("Unexpected error during finish verification execution", e);
        }
    }

}

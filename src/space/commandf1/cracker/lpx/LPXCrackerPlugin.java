package space.commandf1.cracker.lpx;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import space.commandf1.cracker.lpx.util.ClassUtil;
import space.commandf1.cracker.lpx.util.Metrics;
import it.ytnoos.lpx.q1;

/**
 * @author commandf1
 */
public class LPXCrackerPlugin extends q1 {
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
        new Metrics(this, 27278);

        String version = this.getDescription().getVersion();

        this.getLogger().info("LPX v" + version + " was cracked by commandf1");
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
                commandSender.sendMessage("This server is running a NULLED version of LPX v" + version);
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

        byte[] bytes = ClassUtil.modifyClass(classBytes,
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
}
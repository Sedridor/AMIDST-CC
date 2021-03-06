package amidst;

import java.awt.Image;
import java.io.File;
import java.net.MalformedURLException;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import com.google.gson.Gson;
import amidst.gui.version.VersionSelectWindow;
import amidst.logging.FileLogger;
import amidst.logging.Log;
import amidst.minecraft.Minecraft;
import amidst.minecraft.MinecraftUtil;
import amidst.preferences.BiomeColorProfile;
import amidst.project.FinderWindow;
import amidst.resources.ResourceLoader;

public class Amidst
{
    public final static int version_major = 4;
    public final static int version_minor = 1;
    public final static String versionOffset = "";
    public static Image icon = ResourceLoader.getImage("icon.png");
    public static final Gson gson = new Gson();

    public static void main(String args[])
    {
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable e)
            {
                Log.crash(e, "AMIDST has encounted an uncaught exception on thread: " + thread);
            }
        });
        CmdLineParser parser = new CmdLineParser(Options.instance);
        Util.setMinecraftDirectory();
        try
        {
            parser.parseArgument(args);
        }
        catch (CmdLineException e)
        {
            Log.w("There was an issue parsing command line options.");
            e.printStackTrace();
        }

        if (Options.instance.logPath != null)
        {
            Log.addListener("file", new FileLogger(new File(Options.instance.logPath)));
        }

        if (!isOSX())
        {
            Util.setLookAndFeel();
        }
        System.setProperty("sun.java2d.opengl", "True");
        System.setProperty("sun.java2d.accthreshold", "0");
        BiomeColorProfile.scan();

        if (Options.instance.minecraftJar != null)
        {
            try
            {
                Util.setProfileDirectory(Options.instance.minecraftPath);
                MinecraftUtil.setBiomeInterface(new Minecraft(new File(Options.instance.minecraftJar)).createInterface());
                new FinderWindow();
            }
            catch (MalformedURLException e)
            {
                Log.crash(e, "MalformedURLException on Minecraft load.");
            }
        }
        else
        {
            new VersionSelectWindow();
        }
    }

    public static boolean isOSX()
    {
        String osName = System.getProperty("os.name");
        return osName.contains("OS X");
    }

    public static String version()
    {
        if (MinecraftUtil.hasInterface())
        {
            return version_major + "." + version_minor + versionOffset + " [Minecraft version: " + MinecraftUtil.getVersion() + "]";
        }
        return version_major + "." + version_minor + versionOffset;
    }

    public static String getVersion()
    {
        return version_major + "." + version_minor + versionOffset;
    }
}

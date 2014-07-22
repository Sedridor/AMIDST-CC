package amidst.preferences;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import java.util.prefs.PreferencesFactory;
import amidst.Amidst;

public class FilePreferencesGenerator implements PreferencesFactory
{
    private static final Logger log = Logger.getLogger(FilePreferencesGenerator.class.getName());

    private static File preferencesFile;
    private Preferences rootPreferences;
    public static final String SYSTEM_PROPERTY_FILE = "amidst.preferences.FilePreferencesGenerator.file";

    @Override
    public Preferences systemRoot()
    {
        return userRoot();
    }

    @Override
    public Preferences userRoot()
    {
        if (rootPreferences == null)
        {
            log.finer("Instantiating root preferences");
            rootPreferences = new FilePreferences(null, "");
        }
        return rootPreferences;
    }

    public static File getPreferencesFile()
    {
        if (preferencesFile == null)
        {
            String prefsFile = System.getProperty(SYSTEM_PROPERTY_FILE);
            if (prefsFile == null || prefsFile.length() == 0)
            {
                prefsFile = System.getProperty("user.dir") + File.separator + "amidst.cfg";
            }
            preferencesFile = new File(prefsFile).getAbsoluteFile();
            log.finer("Preferences file is " + preferencesFile);
        }
        return preferencesFile;
    }
}

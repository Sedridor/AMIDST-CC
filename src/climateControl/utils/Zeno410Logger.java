package climateControl.utils;

/*
 * author Zeno410
 * using code by Lars Vogel
 * This class currently serves to turn on and off logging by editing code here rather
 * than everywher
 */

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Zeno410Logger
{
    static private FileHandler fileTxt;
    static private SingleLineFormatter formatterTxt;

    static private FileHandler fileHTML;
    static private Formatter formatterHTML;

    public static final boolean suppress = true;
    private Logger logger;

    public static void crashIfRecording(RuntimeException toThrow)
    {
        if (suppress)
        {
            return;
        }
        throw toThrow;
    }

    static public Logger globalLogger()
    {
        Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

        logger.setLevel(Level.ALL);
        if (!suppress)
        {
            try
            {
                fileTxt = new FileHandler("/Zeno410Logging.txt");
                // Create txt Formatter
                formatterTxt = new SingleLineFormatter();
                fileTxt.setFormatter(formatterTxt);
                logger.addHandler(fileTxt);
            }
            catch (IOException ex)
            {
                ex.printStackTrace(System.err);
            }
            catch (SecurityException ex)
            {
                ex.printStackTrace(System.err);
            }
        }
        logger.log(Level.INFO, "Starting");
        return logger;
    }

    public Logger logger()
    {
        return logger;
    }

    public Zeno410Logger(String name)
    {
        logger = Logger.getLogger(name);

        if (logger == null)
        {
            logger = Logger.getAnonymousLogger();
        }
        formatterTxt = new SingleLineFormatter();
        logger.getParent().getHandlers()[0].setFormatter(formatterTxt);

        // if logging is off make the loggers do nothing
        if (suppress)
        {
            return;
        }
        try
        {
            fileTxt = new FileHandler("/" + name + ".txt");
            //formatterTxt = new SingleLineFormatter();
            fileTxt.setFormatter(formatterTxt);
            logger.addHandler(fileTxt);
        }
        catch (IOException ex)
        {
            ex.printStackTrace(System.err);
        }
        catch (SecurityException ex)
        {
            ex.printStackTrace(System.err);
        }
    }
}

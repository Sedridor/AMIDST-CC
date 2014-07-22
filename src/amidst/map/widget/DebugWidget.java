package amidst.map.widget;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import MoF.MapViewer;
import amidst.Options;
import amidst.map.Fragment;
import amidst.map.FragmentManager;

public class DebugWidget extends PanelWidget
{
    public DebugWidget(MapViewer mapViewer)
    {
        super(mapViewer);
        yPadding += 40;
        forceVisibility(onVisibilityCheck());
    }

    @Override
    public void draw(Graphics2D g2d, float time)
    {
        FragmentManager fragmentManager = mapViewer.getFragmentManager();
        ArrayList<String> panelText = new ArrayList<String>();
        panelText.add("Fragment Manager:");
        panelText.add("Pool Size: " + fragmentManager.getCacheSize());
        panelText.add("Free Queue Size: " + fragmentManager.getFreeFragmentQueueSize());
        panelText.add("Request Queue Size: " + fragmentManager.getRequestQueueSize());
        panelText.add("Recycle Queue Size: " + fragmentManager.getRecycleQueueSize());
        panelText.add("");
        panelText.add("Map Viewer:");
        Point centerLocation = new Point((int)(mapViewer.getWidth() * 0.5F), (int)(mapViewer.getHeight() * 0.5F));
        panelText.add("Map Location: " + map.screenToLocal(centerLocation.getLocation()).x + ", " +  + map.screenToLocal(centerLocation.getLocation()).y);
        panelText.add("Map Zoom: " + String.format("%.3f [1 pixel = %.1f blocks]", map.getZoom(), (1 / map.getZoom())));
        panelText.add("Map Size: " + map.tileWidth + "x" + map.tileHeight + " [" + (map.tileWidth * map.tileHeight) + "]");

        int width = 0, height;
        for (int i = 0; i < panelText.size(); i++)
        {
            int textWidth = mapViewer.getFontMetrics().stringWidth(panelText.get(i));
            if (textWidth > width)
            {
                width = textWidth;
            }
        }

        width += 20;
        height = panelText.size() * 20 + 10;

        setDimensions(width, height);
        super.draw(g2d, time);

        g2d.setColor(textColor);
        for (int i = 0; i < panelText.size(); i++)
        {
            g2d.drawString(panelText.get(i), x + 10, y + 20 + i * 20);
        }
    }

    @Override
    protected boolean onVisibilityCheck()
    {
        return Options.instance.showDebug.get();
    }
}

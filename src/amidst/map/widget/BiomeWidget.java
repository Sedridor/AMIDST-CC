package amidst.map.widget;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import javax.swing.ImageIcon;
import amidst.Options;
import amidst.Util;
import amidst.map.layers.BiomeLayer;
import amidst.minecraft.Biome;
import MoF.MapViewer;

public class BiomeWidget extends PanelWidget
{
    private static BiomeWidget instance;
    private static Color innerBoxBgColor = new Color(0.3f, 0.3f, 0.3f, 0.3f);
    private static Color biomeBgColor1 = new Color(0.8f, 0.8f, 0.8f, 0.2f);
    private static Color biomeBgColor2 = new Color(0.6f, 0.6f, 0.6f, 0.2f);
    private static Color biomeLitBgColor1 = new Color(0.8f, 0.8f, 1.0f, 0.7f);
    private static Color biomeLitBgColor2 = new Color(0.6f, 0.6f, 0.8f, 0.7f);
    private static Color innerBoxBorderColor = new Color(1.0f, 1.0f, 1.0f, 1.0f);
    private static Color scrollbarColor = new Color(0.6f, 0.6f, 0.6f, 0.8f);
    private static Color scrollbarLitColor = new Color(0.6f, 0.6f, 0.8f, 0.8f);
    private static Color selectButtonColor = new Color(0.6f, 0.6f, 0.8f, 1.0f);

    private ArrayList<Biome> biomes = new ArrayList<Biome>();
    private int maxNameWidth = 0;
    private Rectangle innerBox = new Rectangle(0, 0, 1, 1);

    private int biomeListHeight;
    private int biomeListYOffset = 0;
    private boolean scrollbarVisible = false;
    private boolean scrollbarGrabbed = false;
    private int scrollbarHeight = 0, scrollbarWidth = 10, scrollbarY = 0, mouseYOnGrab = 0, scrollbarYOnGrab;

    public BiomeWidget(MapViewer mapViewer)
    {
        super(mapViewer);

        FontMetrics fontMetrics = mapViewer.getFontMetrics(textFont);
        for (int i = 0; i < Biome.biomes.length; i++)
        {
            if (Biome.biomes[i] != null)
            {
                maxNameWidth = Math.max(fontMetrics.stringWidth(Biome.biomes[i].name), maxNameWidth);
            }
        }

        setDimensions(250, 400);
        y = 100;
        forceVisibility(false);
    }

    @Override
    public void draw(Graphics2D g2d, float time)
    {
        x = mapViewer.getWidth() - width;
        super.draw(g2d, time);
        g2d.setColor(textColor);
        g2d.setFont(textFont);
        g2d.drawString("Highlight Biomes", x + 10, y + 20);

        innerBox.x = x + 8;
        innerBox.y = y + 30;
        innerBox.width = width - 16;
        innerBox.height = height - 58;

        createSortedBiomeList(Options.instance.sortedBiomeList.get());
        biomeListHeight = biomes.size() * 16;

        biomeListYOffset = Math.min(0, Math.max(-biomeListHeight + innerBox.height, biomeListYOffset));

        if (biomeListHeight > innerBox.height)
        {
            innerBox.width -= scrollbarWidth;
            scrollbarVisible = true;
        }
        else
        {
            scrollbarVisible = false;
        }

        g2d.setColor(innerBoxBgColor);
        g2d.fillRect(innerBox.x, innerBox.y, innerBox.width, innerBox.height);
        g2d.setColor(innerBoxBorderColor);
        g2d.drawRect(innerBox.x - 1, innerBox.y - 1, innerBox.width + 1 + (scrollbarVisible ? scrollbarWidth : 0), innerBox.height + 1);
        g2d.setClip(innerBox);

        for (int i = 0; i < biomes.size(); i++)
        {
            Biome biome = biomes.get(i);
            if (BiomeLayer.instance.isBiomeSelected(biome.index))
            {
                g2d.setColor(((i % 2) == 1) ? biomeLitBgColor1 : biomeLitBgColor2);
            }
            else
            {
                g2d.setColor(((i % 2) == 1) ? biomeBgColor1 : biomeBgColor2);
            }
            g2d.fillRect(innerBox.x, innerBox.y + i * 16 + biomeListYOffset, innerBox.width, 16);
            g2d.setColor(new Color(biome.color));
            g2d.fillRect(innerBox.x, innerBox.y + i * 16 + biomeListYOffset, 20, 16);
            g2d.setColor(Color.white);
            if (Options.instance.showBiomeIDs.get())
            {
                g2d.drawString("[" + biome.index + "] " + biome.name, innerBox.x + 25, innerBox.y + 13 + i * 16 + biomeListYOffset);
            }
            else
            {
                g2d.drawString(biome.name, innerBox.x + 25, innerBox.y + 13 + i * 16 + biomeListYOffset);
            }
        }

        g2d.setClip(null);

        if (scrollbarVisible)
        {
            float boxHeight = innerBox.height;
            float listHeight = biomeListHeight;

            if (scrollbarGrabbed)
            {
                Point mouse = mapViewer.getMousePosition();
                if (mouse != null)
                {
                    int tempScrollbarY = -scrollbarYOnGrab - (mouse.y - mouseYOnGrab);
                    biomeListYOffset = (int)((listHeight / boxHeight) * tempScrollbarY);
                    biomeListYOffset = Math.min(0, Math.max(-biomeListHeight + innerBox.height, biomeListYOffset));
                }
                else
                {
                    scrollbarGrabbed = false;
                }
            }

            float yOffset = -biomeListYOffset;

            scrollbarY = (int)((yOffset / listHeight) * boxHeight);
            scrollbarHeight = (int)(Math.ceil(boxHeight * (boxHeight / listHeight)));
            g2d.setColor(scrollbarGrabbed ? scrollbarLitColor : scrollbarColor);
            g2d.fillRect(innerBox.x + innerBox.width, innerBox.y + scrollbarY, scrollbarWidth, scrollbarHeight);
        }

        g2d.setColor(Color.white);
        g2d.drawString("Select:", x + 8, y + height - 10);
        g2d.setColor(selectButtonColor);
        g2d.drawString("All  Special  None", x + 120, y + height - 10);
    }

    @Override
    public boolean onMouseWheelMoved(int mouseX, int mouseY, int notches)
    {
        if ((mouseX > innerBox.x - x) && (mouseX < innerBox.x - x + innerBox.width) && (mouseY > innerBox.y - y) && (mouseY < innerBox.y - y + innerBox.height))
        {
            biomeListYOffset = Math.min(0, Math.max(-biomeListHeight + innerBox.height, biomeListYOffset - notches * 35));
        }
        return true;
    }

    @Override
    public void onMouseReleased()
    {
        scrollbarGrabbed = false;
    }

    @Override
    public boolean onMousePressed(int mouseX, int mouseY)
    {
        if (scrollbarVisible)
        {
            if ((mouseX >= innerBox.x - x + innerBox.width) && (mouseX < innerBox.x - x + innerBox.width + scrollbarWidth) && (mouseY >= innerBox.y - y + scrollbarY) && (mouseY < innerBox.y - y + scrollbarY + scrollbarHeight))
            {
                mouseYOnGrab = mouseY + y;
                scrollbarYOnGrab = scrollbarY;
                scrollbarGrabbed = true;
            }
            else if ((mouseX >= innerBox.x - x + innerBox.width) && (mouseX < innerBox.x - x + innerBox.width + scrollbarWidth) && (mouseY > innerBox.y - y + scrollbarY + scrollbarHeight) && (mouseY < 512))
            {
                biomeListYOffset = Math.min(0, Math.max(-biomeListHeight + innerBox.height, biomeListYOffset - 400));
            }

            else if ((mouseX >= innerBox.x - x + innerBox.width) && (mouseX < innerBox.x - x + innerBox.width + scrollbarWidth) && (mouseY >= 30) && (mouseY < innerBox.y - y + scrollbarY))
            {
                biomeListYOffset = Math.min(0, Math.max(-biomeListHeight + innerBox.height, biomeListYOffset + 400));
            }
        }

        boolean needsRedraw = false;
        if ((mouseX > innerBox.x - x) && (mouseX < innerBox.x - x + innerBox.width) && (mouseY > innerBox.y - y) && (mouseY < innerBox.y - y + innerBox.height))
        {
            int id = (mouseY - (innerBox.y - y) - biomeListYOffset) / 16;
            if (id < biomes.size())
            {
                BiomeLayer.instance.toggleBiomeSelect(biomes.get(id).index);
                needsRedraw = true;
            }
        }

        // TODO: These values are temporarly hard coded for the sake of a fast release
        if ((mouseY > height - 25) && (mouseY < height - 9))
        {
            if ((mouseX > 117) && (mouseX < 139))
            {
                BiomeLayer.instance.selectAllBiomes();
                needsRedraw = true;
            }
            else if ((mouseX > 143) && (mouseX < 197))
            {
                for (int i = 128; i < Biome.biomes.length; i++)
                {
                    if (Biome.biomes[i] != null)
                    {
                        BiomeLayer.instance.selectBiome(i);
                    }
                }
                needsRedraw = true;
            }
            else if ((mouseX > 203) && (mouseX < 242))
            {
                BiomeLayer.instance.deselectAllBiomes();
                needsRedraw = true;
            }
        }
        if (needsRedraw)
        {
            (new Thread(new Runnable() {
                @Override
                public void run()
                {
                    map.resetImageLayer(BiomeLayer.instance.getLayerId());
                }
            })).start();
        }
        return true;
    }

    @Override
    public boolean onVisibilityCheck()
    {
        height = Math.max(200, mapViewer.getHeight() - 200);
        return BiomeToggleWidget.isBiomeWidgetVisible & (height > 200);
    }

    private void setMapViewer(MapViewer mapViewer)
    {
        this.mapViewer = mapViewer;
        this.map = mapViewer.getMap();
        scrollbarGrabbed = false;
    }

    public static BiomeWidget get(MapViewer mapViewer)
    {
        if (instance == null)
        {
            instance = new BiomeWidget(mapViewer);
        }
        else
        {
            instance.setMapViewer(mapViewer);
        }
        return instance;
    }

    public void createSortedBiomeList(boolean sortedBiomeList)
    {
        if (sortedBiomeList)
        {
            if (biomes.size() > 1 && biomes.get(1).index == Biome.deepOcean.index)
            {
                return;
            }
            biomes.clear();

            biomes.add(Biome.ocean);
            biomes.add(Biome.deepOcean);
            biomes.add(Biome.frozenOcean);

            biomes.add(Biome.plains);

            biomes.add(Biome.forest);
            biomes.add(Biome.forestHills);
            biomes.add(Biome.birchForest);
            biomes.add(Biome.birchForestHills);

            biomes.add(Biome.taiga);
            biomes.add(Biome.taigaHills);
            biomes.add(Biome.coldTaiga);
            biomes.add(Biome.coldTaigaHills);
            biomes.add(Biome.megaTaiga);
            biomes.add(Biome.megaTaigaHills);
            biomes.add(Biome.megaSpruceTaiga);
            biomes.add(Biome.megaSpurceTaigaHills);

            biomes.add(Biome.icePlains);
            biomes.add(Biome.icePlainsSpikes);
            biomes.add(Biome.iceMountains);

            biomes.add(Biome.extremeHills);
            biomes.add(Biome.extremeHillsEdge);
            biomes.add(Biome.extremeHillsPlus);

            biomes.add(Biome.jungle);
            biomes.add(Biome.jungleHills);
            biomes.add(Biome.jungleEdge);

            biomes.add(Biome.savanna);
            biomes.add(Biome.savannaPlateau);

            biomes.add(Biome.desert);
            biomes.add(Biome.desertHills);

            biomes.add(Biome.mesa);
            biomes.add(Biome.mesaPlateau);
            biomes.add(Biome.mesaPlateauF);

            biomes.add(Biome.roofedForest);
            biomes.add(Biome.swampland);

            biomes.add(Biome.beach);
            biomes.add(Biome.coldBeach);
            biomes.add(Biome.stoneBeach);
            biomes.add(Biome.river);
            biomes.add(Biome.frozenRiver);

            biomes.add(Biome.mushroomIsland);
            biomes.add(Biome.mushroomIslandShore);

            biomes.add(Biome.oceanM);
            biomes.add(Biome.deepOceanM);
            biomes.add(Biome.frozenOceanM);

            biomes.add(Biome.sunflowerPlains);

            biomes.add(Biome.flowerForest);
            biomes.add(Biome.forestHillsM);
            biomes.add(Biome.birchForestM);
            biomes.add(Biome.birchForestHillsM);
            biomes.add(Biome.taigaM);
            biomes.add(Biome.taigaHillsM);
            biomes.add(Biome.coldTaigaM);
            biomes.add(Biome.coldTaigaHillsM);

            biomes.add(Biome.iceMountainsM);
            biomes.add(Biome.extremeHillsM);
            biomes.add(Biome.extremeHillsEdgeM);
            biomes.add(Biome.extremeHillsPlusM);

            biomes.add(Biome.jungleM);
            biomes.add(Biome.jungleHillsM);
            biomes.add(Biome.jungleEdgeM);
            biomes.add(Biome.savannaM);
            biomes.add(Biome.savannaPlateauM);

            biomes.add(Biome.desertM);
            biomes.add(Biome.desertHillsM);
            biomes.add(Biome.mesaPlateauM);
            biomes.add(Biome.mesaPlateauFM);
            biomes.add(Biome.mesaBryce);

            biomes.add(Biome.roofedForestM);
            biomes.add(Biome.swamplandM);

            biomes.add(Biome.beachM);
            biomes.add(Biome.coldBeachM);
            biomes.add(Biome.stoneBeachM);

            biomes.add(Biome.riverM);
            biomes.add(Biome.frozenRiverM);
            biomes.add(Biome.mushroomIslandM);
            biomes.add(Biome.mushroomIslandShoreM);
        }
        else
        {
            if (biomes.size() > 1 && biomes.get(1).index == Biome.biomes[1].index)
            {
                return;
            }
            biomes.clear();
            for (int i = 0; i < Biome.biomes.length; i++)
            {
                if (Biome.biomes[i] != null)
                {
                    biomes.add(Biome.biomes[i]);
                }
            }
        }
    }
}

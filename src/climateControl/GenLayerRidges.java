package climateControl;

import genLayerPack.GenLayer;
import genLayerPack.IntCache;

/**
 * 
 * 
 * @author Zeno410
 */
public class GenLayerRidges extends GenLayer
{

    private final int biomeModulo; // this modulo must be zero in the biome to generate any ridgeline
    private final int adjacentModulo; // this modulo must be zero in an adjacent biome
    // for the biome to have a ridgeline along the edge
    private final int ridgeValue; // the value for ridges

    public GenLayerRidges(long par1, GenLayer par3GenLayer, int biomeModulo, int adjacentModulo, int ridgeValue)
    {
        super(par1);
        this.parent = par3GenLayer;
        this.biomeModulo = biomeModulo;
        this.adjacentModulo = adjacentModulo;
        this.ridgeValue = ridgeValue;
    }

    private final boolean ridgeBiome(int id)
    {
        return id % biomeModulo == 0;
    }

    private final boolean adjacent(int id)
    {
        return id % adjacentModulo == 0;
    }

    @Override
    public int[] getInts(int par1, int par2, int par3, int par4)
    {
        int i1 = par1 - 1;
        int j1 = par2 - 1;
        int k1 = par3 + 2;
        int l1 = par4 + 2;
        int[] aint = this.parent.getInts(i1, j1, k1, l1);
        int[] aint1 = IntCache.getIntCache(par3 * par4);

        for (int i2 = 0; i2 < par4; ++i2)
        {
            for (int j2 = 0; j2 < par3; ++j2)
            {
                int k2 = aint[j2 + 0 + (i2 + 0) * k1];
                int l2 = aint[j2 + 2 + (i2 + 0) * k1];
                int i3 = aint[j2 + 0 + (i2 + 2) * k1];
                int j3 = aint[j2 + 2 + (i2 + 2) * k1];
                int k3 = aint[j2 + 1 + (i2 + 1) * k1];
                this.initChunkSeed(j2 + par1, i2 + par2);

                if ((k2 == k3) && (l2 == k3) && (i3 == k3) && (j3 == k3))
                {
                    // not on an edge; no ridge
                    aint1[j2 + i2 * par3] = 0;
                    continue;
                }

                if (ridgeBiome(k3) && ((adjacent(k2) && k2 != k3) || (adjacent(l2) && l2 != k3) || (adjacent(i3) && i3 != k3) || (adjacent(j3) && j3 != k3)))
                {
                    aint1[j2 + i2 * par3] = ridgeValue;
                }
                else
                {
                    aint1[j2 + i2 * par3] = 0;
                }
            }
        }

        return aint1;
    }
}

package climateControl;

/**
 * Like GenLayerHills but it removes those derpy little islands that clutter up the deep ocean
 * They occur in *shallow* ocean instead
 * @author Zeno410
 */

import genLayerPack.GenLayer;
import genLayerPack.IntCache;
import genLayerPack.BiomeGenBase;

public class GenLayerTransbiomeHills extends GenLayer
{
    private GenLayer field_151628_d;
    private GenLayer heights;
    private static final String __OBFID = "CL_00000563";

    public GenLayerTransbiomeHills(long p_i45479_1_, GenLayer p_i45479_3_, GenLayer p_i45479_4_, GenLayer heights)
    {
        super(p_i45479_1_);
        this.parent = p_i45479_3_;
        this.field_151628_d = p_i45479_4_;
        this.heights = heights;
    }

    /**
     * Returns a list of integer values generated by this layer. These may be interpreted as temperatures, rainfall amounts, or biomeList[] indices based on the particular GenLayer subclass.
     */
    @Override
    public int[] getInts(int par1, int par2, int par3, int par4)
    {
        int[] aint = this.parent.getInts(par1 - 1, par2 - 1, par3 + 2, par4 + 2);
        int[] aint1 = this.field_151628_d.getInts(par1 - 1, par2 - 1, par3 + 2, par4 + 2);
        int[] aint2 = IntCache.getIntCache(par3 * par4);
        int[] heightNumbers = heights.getInts(par1 - 1, par2 - 1, par3 + 2, par4 + 2);

        for (int i1 = 0; i1 < par4; ++i1)
        {
            for (int j1 = 0; j1 < par3; ++j1)
            {
                this.initChunkSeed(j1 + par1, i1 + par2);
                int k1 = aint[j1 + 1 + (i1 + 1) * (par3 + 2)];
                int l1 = aint1[j1 + 1 + (i1 + 1) * (par3 + 2)];
                int height = heightNumbers[j1 + 1 + (i1 + 1) * (par3 + 2)];
                boolean flag = (l1 - 2) % 29 == 0;

                /*
                 * if (k1 != 0 && l1 >= 2 && (l1 - 2) % 29 == 1 && k1 < 128) { if (BiomeGenBase.getBiome(k1 + 128) != null) { aint2[j1 + i1 * par3] = k1 + 128; } else { aint2[j1 + i1 * par3] = k1; } } else
                 */
                if (height == -1)
                {
                    aint2[j1 + i1 * par3] = BiomeGenBase.deepOcean.biomeID;
                    continue;
                }
                if (height == 0)
                {
                    aint2[j1 + i1 * par3] = 0;
                    continue;
                }
                if (height == 1 && !flag)
                {
                    aint2[j1 + i1 * par3] = k1;
                    continue;
                }
                else
                {
                    int i2 = k1;
                    int j2;

                    if (k1 == BiomeGenBase.desert.biomeID)
                    {
                        i2 = BiomeGenBase.desertHills.biomeID;
                    }
                    else if (k1 == BiomeGenBase.forest.biomeID)
                    {
                        i2 = BiomeGenBase.forestHills.biomeID;
                    }
                    else if (k1 == BiomeGenBase.birchForest.biomeID)
                    {
                        i2 = BiomeGenBase.birchForestHills.biomeID;
                    }
                    else if (k1 == BiomeGenBase.roofedForest.biomeID)
                    {
                        i2 = BiomeGenBase.forestHills.biomeID;
                    }
                    else if (k1 == BiomeGenBase.taiga.biomeID)
                    {
                        i2 = BiomeGenBase.taigaHills.biomeID;
                    }
                    else if (k1 == BiomeGenBase.megaTaiga.biomeID)
                    {
                        i2 = BiomeGenBase.megaTaigaHills.biomeID;
                    }
                    else if (k1 == BiomeGenBase.coldTaiga.biomeID)
                    {
                        i2 = BiomeGenBase.coldTaigaHills.biomeID;
                    }
                    else if (k1 == BiomeGenBase.plains.biomeID)
                    {
                        if (this.nextInt(3) == 0)
                        {
                            i2 = BiomeGenBase.forestHills.biomeID;
                        }
                        else
                        {
                            i2 = BiomeGenBase.extremeHills.biomeID;
                        }
                    }
                    else if (k1 == BiomeGenBase.icePlains.biomeID)
                    {
                        i2 = BiomeGenBase.iceMountains.biomeID;
                    }
                    else if (k1 == BiomeGenBase.swampland.biomeID)
                    {
                        i2 = BiomeGenBase.jungleHills.biomeID;
                    }
                    else if (k1 == BiomeGenBase.jungle.biomeID)
                    {
                        i2 = BiomeGenBase.jungleHills.biomeID;
                    }
                    else if (k1 == BiomeGenBase.ocean.biomeID && this.nextInt(3) == 0)
                    {
                        j2 = this.nextInt(2);

                        if (j2 == 0)
                        {
                            i2 = BiomeGenBase.plains.biomeID;
                        }
                        else
                        {
                            i2 = BiomeGenBase.forest.biomeID;
                        }
                    }
                    else if (k1 == BiomeGenBase.extremeHills.biomeID)
                    {
                        i2 = BiomeGenBase.extremeHillsPlus.biomeID;
                    }
                    else if (k1 == BiomeGenBase.savanna.biomeID)
                    {
                        i2 = BiomeGenBase.savannaPlateau.biomeID;
                    }
                    else if (compareBiomesById(k1, BiomeGenBase.mesaPlateau_F.biomeID))
                    {
                        i2 = BiomeGenBase.mesa.biomeID;
                    }
                    else if (k1 == BiomeGenBase.deepOcean.biomeID && this.nextInt(3) == 0)
                    {

                    }

                    if (flag && i2 != k1)
                    {
                        if (BiomeGenBase.getBiome(i2 + 128) != null)
                        {
                            i2 += 128;
                        }
                        else
                        {
                            i2 = k1;
                        }
                    }

                    aint2[j1 + i1 * par3] = i2;
                }
            }
        }

        return aint2;
    }
}
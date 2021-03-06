package climateControl;

import genLayerPack.GenLayer;
import genLayerPack.IntCache;

/**
 * 
 * @author Zeno410
 */
public class GenLayerAddShore extends GenLayer
{
    private static final String __OBFID = "CL_00000551";

    public GenLayerAddShore(long par1, GenLayer par3GenLayer)
    {
        super(par1);
        this.parent = par3GenLayer;
    }

    /**
     * Returns a list of integer values generated by this layer. These may be interpreted as temperatures, rainfall amounts, or biomeList[] indices based on the particular GenLayer subclass.
     */
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

                if (k3 == 0 && (k2 != 0 || l2 != 0 || i3 != 0 || j3 != 0))
                {

                    if (this.nextInt(3) == 0)
                    {
                        aint1[j2 + i2 * par3] = 1;
                    }
                    else
                    {
                        aint1[j2 + i2 * par3] = 0;
                    }
                }
                else if (k3 == 1 && (k2 == 0 || l2 == 0 || i3 == 0 || j3 == 0))
                {
                    if (this.nextInt(5) == 0)
                    {
                        if (k3 == 4)
                        {
                            aint1[j2 + i2 * par3] = 0;
                        }
                        else
                        {
                            aint1[j2 + i2 * par3] = 0;
                        }
                    }
                    else
                    {
                        aint1[j2 + i2 * par3] = k3;
                    }
                }
                else
                {
                    aint1[j2 + i2 * par3] = k3;
                }
            }
        }

        return aint1;
    }
}
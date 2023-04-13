package net.minecraft.world.gen.layer;

public class GenLayerRiverInit extends GenLayer
{
    public GenLayerRiverInit(long p_i2127_1_, GenLayer p_i2127_3_)
    {
        super(p_i2127_1_);
        this.parent = p_i2127_3_;
    }

    /**
     * Returns a list of integer values generated by this layer. These may be interpreted as temperatures, rainfall
     * amounts, or Biome ID's based on the particular GenLayer subclass.
     */
    public int[] getInts(int areaX, int areaY, int areaWidth, int areaHeight)
    {
        int[] aint = this.parent.getInts(areaX, areaY, areaWidth, areaHeight);
        int[] aint1 = IntCache.getIntCache(areaWidth * areaHeight);

        for (int i = 0; i < areaHeight; ++i)
        {
            for (int j = 0; j < areaWidth; ++j)
            {
                this.initChunkSeed((long)(j + areaX), (long)(i + areaY));
                aint1[j + i * areaWidth] = aint[j + i * areaWidth] > 0 ? this.nextInt(299999) + 2 : 0;
            }
        }

        return aint1;
    }
}
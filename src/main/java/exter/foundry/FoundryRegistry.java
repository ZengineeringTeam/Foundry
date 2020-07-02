package exter.foundry;

import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class FoundryRegistry
{
    public static final String CLAYBLOCK = "foundryClayblock";

    public static final String BARREL = "foundryBarrel";

    public static final Block CRYOTHEUM = ForgeRegistries.BLOCKS
            .getValue(new ResourceLocation("thermalfoundation:fluid_cryotheum"));
}

package exter.foundry;

import exter.foundry.config.FoundryConfig;
import exter.foundry.util.MiscUtil;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class FoundryRegistry
{
    public static final ItemStack CLAYBLOCK = MiscUtil.parseItem(FoundryConfig.CLAYBLOCK);

    public static final ItemStack BARREL = MiscUtil.parseItem(FoundryConfig.BARREL);

    public static final Block CRYOTHEUM = ForgeRegistries.BLOCKS
            .getValue(new ResourceLocation("thermalfoundation:fluid_cryotheum"));
}

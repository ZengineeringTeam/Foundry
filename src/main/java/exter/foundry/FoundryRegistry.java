package exter.foundry;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.registry.GameRegistry.ItemStackHolder;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;

@EventBusSubscriber
public class FoundryRegistry
{
    @ItemStackHolder(value = "ceramics:unfired_clay", meta = 4)
    public static final ItemStack CLAY = ItemStack.EMPTY;

    @ItemStackHolder(value = "ceramics:unfired_clay", meta = 5)
    public static final ItemStack BRICK = ItemStack.EMPTY;

    @ItemStackHolder("ceramics:clay_soft")
    public static final ItemStack CLAYBLOCK = ItemStack.EMPTY;

    @ItemStackHolder("ceramics:porcelain_barrel")
    public static final ItemStack BARREL = ItemStack.EMPTY;

    @ObjectHolder("thermalfoundation:fluid_cryotheum")
    public static final Block CRYOTHEUM = null;
}

package exter.foundry;

import java.util.List;

import exter.foundry.init.InitRecipes;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ItemStackHolder;

@EventBusSubscriber
public class FoundryRegistry
{

    public static final List<Block> BLOCKS = Foundry.INFO.getBlockList();
    public static final List<Item> ITEMS = Foundry.INFO.getItemList();
    public static final List<IRecipe> RECIPES = Foundry.INFO.getRecipeList();

    @SubscribeEvent
    public void registerBlocks(Register<Block> e)
    {
        e.getRegistry().registerAll(BLOCKS.toArray(new Block[BLOCKS.size()]));
    }

    @SubscribeEvent
    public void registerItems(Register<Item> e)
    {
        e.getRegistry().registerAll(ITEMS.toArray(new Item[ITEMS.size()]));
    }

    @SubscribeEvent
    public void registerRecipes(Register<IRecipe> e)
    {
        InitRecipes.preInit();
        e.getRegistry().registerAll(RECIPES.toArray(new IRecipe[RECIPES.size()]));
    }

    @ItemStackHolder(value = "ceramics:unfired_clay", meta = 4)
    public static final ItemStack CLAY = ItemStack.EMPTY;

    @ItemStackHolder(value = "ceramics:unfired_clay", meta = 5)
    public static final ItemStack BRICK = ItemStack.EMPTY;

    @ItemStackHolder("ceramics:clay_soft")
    public static final ItemStack CLAYBLOCK = ItemStack.EMPTY;

    @ItemStackHolder("ceramics:clay_hard")
    public static final ItemStack BRICKBLOCK = ItemStack.EMPTY;
}

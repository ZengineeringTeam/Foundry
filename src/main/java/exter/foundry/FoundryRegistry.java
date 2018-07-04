package exter.foundry;

import java.util.List;

import exter.foundry.init.InitRecipes;
import exter.foundry.item.FoundryItems;
import exter.foundry.item.ItemComponent;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.OreDictionary;

@EventBusSubscriber
public class FoundryRegistry {

	public static final List<Block> BLOCKS = Foundry.INFO.getBlockList();
	public static final List<Item> ITEMS = Foundry.INFO.getItemList();
	public static final List<IRecipe> RECIPES = Foundry.INFO.getRecipeList();

	@SubscribeEvent
	public void registerBlocks(Register<Block> e) {
		e.getRegistry().registerAll(BLOCKS.toArray(new Block[BLOCKS.size()]));
	}

	@SubscribeEvent
	public void registerItems(Register<Item> e) {
		e.getRegistry().registerAll(ITEMS.toArray(new Item[ITEMS.size()]));
	}

	@SubscribeEvent
	public void registerRecipes(Register<IRecipe> e) {
		OreDictionary.registerOre("fuelCoke", FoundryItems.component(ItemComponent.SubItem.COAL_COKE));
		OreDictionary.registerOre("rodCupronickel", FoundryItems.component(ItemComponent.SubItem.ROD_CUPRONICKEL));
		InitRecipes.preInit();
		e.getRegistry().registerAll(RECIPES.toArray(new IRecipe[RECIPES.size()]));
	}
}

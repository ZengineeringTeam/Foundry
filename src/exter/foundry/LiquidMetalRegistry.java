package exter.foundry;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import exter.foundry.block.BlockAlloyMixer;
import exter.foundry.block.BlockLiquidMetal;
import exter.foundry.block.BlockMetal;
import exter.foundry.block.BlockMetalCaster;
import exter.foundry.block.BlockInductionCrucibleFurnace;
import exter.foundry.item.FoundryItems;
import exter.foundry.item.ItemFoundryComponent;
import exter.foundry.item.ItemFoundryContainer;
import exter.foundry.item.ItemMold;
import exter.foundry.recipes.AlloyRecipe;
import exter.foundry.recipes.CastingRecipe;
import exter.foundry.recipes.MeltingRecipe;
import exter.foundry.renderer.RendererItemContainer;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

public class LiquidMetalRegistry
{
  static private Map<String,LiquidMetalRegistry> registry = new HashMap<String,LiquidMetalRegistry>();
  
  
  public final Block block;

  public final Fluid fluid;
  
  public final String name;
  
  private LiquidMetalRegistry(Block liquid_block,Fluid liquid_fluid,String metal_name)
  {
    block = liquid_block;
    fluid = liquid_fluid;
    name = metal_name;

  }
  
  /**
   * Helper method to register a metal's fluid, block, smelting, and casting.
   * @param config Forge Configuration file.
   * @param metal_name Name of the metal e.g "Copper" for "oreCopper" in the Ore Dictionary.
   * @param default_block_id Default block id of the fluid block.
   * @param default_container_id Default item id of the fluid container.
   */
  static public void RegisterLiquidMetal(Configuration config,String metal_name,int default_block_id,int temperature,int luminosity)
  {
    int i;
    int block_id = config.getBlock("liquid" + metal_name, default_block_id).getInt();

    Fluid fluid = new Fluid("liquid" + metal_name).setTemperature(temperature).setLuminosity(luminosity).setDensity(2000);
    FluidRegistry.registerFluid(fluid);

    List<ItemStack> ore_list = OreDictionary.getOres("block" + metal_name);
    int solid_block = -1;
    int solid_meta = 0;
    for(ItemStack ore:ore_list)
    {
      Item item = ore.getItem();
      if(item instanceof ItemBlock)
      {
        solid_block = ((ItemBlock)item).getBlockID();
        solid_meta = ((ItemBlock)item).getMetadata(ore.getItemDamage());
      }
    }
    Block liquid_block = new BlockLiquidMetal(block_id, fluid, Material.lava,"liquid" + metal_name,solid_block,solid_meta);
    liquid_block.setUnlocalizedName("liquid" + metal_name);
    LanguageRegistry.addName(liquid_block, "Liquid " + metal_name);
    GameRegistry.registerBlock(liquid_block, "liquid" + metal_name);

    fluid.setBlockID(liquid_block);

    MeltingRecipe.RegisterBasicRecipes(metal_name,fluid);

    ItemStack mold_ingot = new ItemStack(FoundryItems.item_mold,1,ItemMold.MOLD_INGOT);
    ItemStack mold_block = new ItemStack(FoundryItems.item_mold,1,ItemMold.MOLD_BLOCK);    
    
    CastingRecipe.RegisterRecipe("block" + metal_name, new FluidStack(fluid,MeltingRecipe.AMOUNT_BLOCK), mold_block, null);
    CastingRecipe.RegisterRecipe("ingot" + metal_name, new FluidStack(fluid,MeltingRecipe.AMOUNT_INGOT), mold_ingot, null);



    LiquidMetalRegistry metal = new LiquidMetalRegistry(liquid_block,fluid,metal_name);
    
    MinecraftForge.EVENT_BUS.register(metal);
    
    registry.put(metal_name, metal);
  }
  
  /**
   * @param name Name the liquid metal was registered in, e.g. "Copper".
   * @return The liquid metal registry containing the fluid, and fluid block
   */
  static public LiquidMetalRegistry GetMetal(String name)
  {
    return registry.get(name);
  }
 
  static public LiquidMetalRegistry GetMetal(Fluid fluid)
  {
    for(LiquidMetalRegistry reg:registry.values())
    {
       if(reg != null)
       {
         if(reg.fluid.getID() == fluid.getID())
         {
           return reg;
         }
       }
    }
    return null;
  }

  @ForgeSubscribe
  @SideOnly(Side.CLIENT)
  public void textureHook(TextureStitchEvent.Post event)
  {
    if(event.map.textureType == 0)
    {
      fluid.setIcons(block.getBlockTextureFromSide(1));
    }
  }
  
  @Override
  public int hashCode()
  {
    return ("LiquidMetal_"+name).hashCode();
  }
  
  @Override
  public boolean equals(Object obj)
  {
    return obj instanceof LiquidMetalRegistry && hashCode() == obj.hashCode();
  }
}

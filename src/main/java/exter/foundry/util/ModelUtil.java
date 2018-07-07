package exter.foundry.util;

import javax.annotation.Nonnull;

import exter.foundry.Foundry;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public final class ModelUtil
{

    private ModelUtil()
    {
    }

    public static void mapItemModel(Block block)
    {
        mapItemModel(Item.getItemFromBlock(block));
    }

    public static void mapItemModel(Item item)
    {
        ModelLoader.setCustomModelResourceLocation(item, 0,
                new ModelResourceLocation(item.getRegistryName().toString(), "inventory"));
    }

    public static void mapItemModel(Item item, @Nonnull String customPath)
    {
        ModelLoader.setCustomModelResourceLocation(item, 0,
                new ModelResourceLocation(Foundry.MODID + ":" + customPath, "inventory"));
    }

    public static void mapItemVariantsModel(Block block, String suffix, IStringSerializable[] types)
    {
        mapItemVariantsModel(Item.getItemFromBlock(block), suffix, types);
    }

    public static void mapItemVariantsModel(Item item, String suffix, IStringSerializable[] types)
    {
        for (int i = 0; i < types.length; i++)
        {
            ModelLoader.setCustomModelResourceLocation(item, i,
                    new ModelResourceLocation(Foundry.MODID + ":" + types[i].getName() + suffix, "inventory"));
        }
    }

    public static void mapFluidModel(BlockFluidBase fluidBlock)
    {
        Fluid fluid = fluidBlock.getFluid();
        FluidCustomModelMapper mapper = new FluidCustomModelMapper(fluid);
        ModelLoader.setCustomStateMapper(fluidBlock, mapper);
        Item item = Item.getItemFromBlock(fluidBlock);
        ModelBakery.registerItemVariants(item);
        ModelLoader.setCustomMeshDefinition(item, mapper);
    }

    public static class FluidCustomModelMapper extends StateMapperBase implements ItemMeshDefinition
    {

        private final ModelResourceLocation res;

        public FluidCustomModelMapper(Fluid f)
        {
            this.res = new ModelResourceLocation(Foundry.MODID + ":fluid", f.getName());
        }

        @Override
        public ModelResourceLocation getModelLocation(ItemStack stack)
        {
            return res;
        }

        @Override
        public ModelResourceLocation getModelResourceLocation(IBlockState state)
        {
            return res;
        }

    }
}

package exter.foundry.client;

import exter.foundry.Foundry;
import exter.foundry.fluid.FoundryFluidRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@EventBusSubscriber(Side.CLIENT)
public class BlockRendering
{
    @SubscribeEvent
    public static void onModelRegister(ModelRegistryEvent event)
    {

        FoundryFluidRegistry.getMap().forEach((name, strategy) -> {
            if (strategy.enabled())
            {
                Block block = FluidRegistry.getFluid(name).getBlock();
                if (block instanceof BlockFluidBase)
                {
                    mapFluidModel((BlockFluidBase) block);
                }
            }
        });
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

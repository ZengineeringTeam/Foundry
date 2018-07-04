package exter.foundry.proxy;

import java.util.List;
import java.util.Map;

import exter.foundry.FoundryRegistry;
import exter.foundry.block.BlockCastingTable;
import exter.foundry.block.BlockComponent;
import exter.foundry.block.BlockFoundryMachine;
import exter.foundry.block.BlockLiquidMetal;
import exter.foundry.block.FoundryBlocks;
import exter.foundry.fluid.FluidLiquidMetal;
import exter.foundry.fluid.LiquidMetalRegistry;
import exter.foundry.integration.ModIntegrationManager;
import exter.foundry.item.FoundryItems;
import exter.foundry.item.ItemMold;
import exter.foundry.material.MaterialRegistry;
import exter.foundry.material.OreDictMaterial;
import exter.foundry.material.OreDictType;
import exter.foundry.tileentity.TileEntityCastingTableBlock;
import exter.foundry.tileentity.TileEntityCastingTableIngot;
import exter.foundry.tileentity.TileEntityCastingTablePlate;
import exter.foundry.tileentity.TileEntityCastingTableRod;
import exter.foundry.tileentity.TileEntityRefractoryHopper;
import exter.foundry.tileentity.TileEntityRefractorySpout;
import exter.foundry.tileentity.renderer.CastingTableRenderer;
import exter.foundry.tileentity.renderer.CastingTableRendererBlock;
import exter.foundry.tileentity.renderer.HopperRenderer;
import exter.foundry.tileentity.renderer.SpoutRenderer;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.OreDictionary;

public class ClientFoundryProxy extends CommonFoundryProxy
{
    public static class FluidStateMapper extends StateMapperBase implements ItemMeshDefinition
    {

        public final BlockLiquidMetal fluid;
        public final ModelResourceLocation location;

        public FluidStateMapper(BlockLiquidMetal fluid)
        {
            this.fluid = fluid;
            this.location = new ModelResourceLocation(fluid.getRegistryName(), "normal");
        }

        @Override
        public ModelResourceLocation getModelLocation(ItemStack stack)
        {
            return this.location;
        }

        @Override
        protected ModelResourceLocation getModelResourceLocation(IBlockState state)
        {
            return this.location;
        }
    }

    static private class LiquidMetalItemMeshDefinition implements ItemMeshDefinition
    {
        private final ModelResourceLocation model;

        LiquidMetalItemMeshDefinition(String name)
        {
            model = new ModelResourceLocation("foundry:liquid" + name);
        }

        @Override
        public ModelResourceLocation getModelLocation(ItemStack stack)
        {
            return model;
        }
    }

    @SubscribeEvent
    public void doModels(ModelRegistryEvent e)
    {
        for (BlockFoundryMachine.EnumMachine m : BlockFoundryMachine.EnumMachine.values())
        {
            registerItemModel(FoundryBlocks.block_machine, m.model, m.id);
        }
        for (BlockCastingTable.EnumTable m : BlockCastingTable.EnumTable.values())
        {
            registerItemModel(FoundryBlocks.block_casting_table, m.model, m.id);
        }

        registerItemModel(FoundryBlocks.block_refractory_glass, "refractoryGlass");
        registerItemModel(FoundryBlocks.block_mold_station, "moldStation");
        registerItemModel(FoundryBlocks.block_refractory_hopper, "refractoryHopper");
        registerItemModel(FoundryBlocks.block_burner_heater, "burnerHeater");
        registerItemModel(FoundryBlocks.block_refractory_spout, "refractorySpout");
        registerItemModel(FoundryBlocks.block_cauldron_bronze, "bronzeCauldron");

        for (BlockComponent.EnumVariant v : BlockComponent.EnumVariant.values())
        {
            registerItemModel(FoundryBlocks.block_component, v.model, v.ordinal());
        }

        registerItemModel(FoundryItems.item_small_clay, "small_clay");

        for (ItemMold.SubItem m : ItemMold.SubItem.values())
        {
            registerItemModel(FoundryItems.item_mold, m.name, m.id);
        }

        for (Block b : FoundryRegistry.BLOCKS)
        {
            if (b instanceof BlockLiquidMetal)
            {
                ModelLoader.setCustomStateMapper(b, new FluidStateMapper((BlockLiquidMetal) b));
                ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(b), 0,
                        new ModelResourceLocation(b.getRegistryName(), "normal"));
            }
        }
    }

    @Override
    public void init()
    {

        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCastingTableIngot.class,
                new CastingTableRenderer(6, 10, 4, 12, 9, 12, "foundry:blocks/castingtable_top_ingot"));
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCastingTablePlate.class,
                new CastingTableRenderer(3, 13, 3, 13, 11, 12, "foundry:blocks/castingtable_top_plate"));
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCastingTableRod.class,
                new CastingTableRenderer(7, 9, 2, 14, 10, 12, "foundry:blocks/castingtable_top_rod"));
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCastingTableBlock.class,
                new CastingTableRendererBlock());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityRefractorySpout.class, new SpoutRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityRefractoryHopper.class, new HopperRenderer());

        ModIntegrationManager.clientInit();
    }

    @Override
    public void postInit()
    {
        for (OreDictMaterial material : OreDictMaterial.MATERIALS)
        {
            List<ItemStack> ores = OreDictionary.getOres(material.default_prefix + material.suffix, false);
            if (ores.size() > 0)
            {
                MaterialRegistry.INSTANCE.registerMaterialIcon(material.suffix, ores.get(0));
            }
            else
            {
                for (OreDictType type : OreDictType.TYPES)
                {
                    ores = OreDictionary.getOres(type.prefix + material.suffix, false);
                    if (ores.size() > 0)
                    {
                        MaterialRegistry.INSTANCE.registerMaterialIcon(material.suffix, ores.get(0));
                        break;
                    }
                }
            }
        }

        for (OreDictType type : OreDictType.TYPES)
        {
            List<ItemStack> ores = OreDictionary.getOres(type.prefix + type.default_suffix, false);
            if (ores.size() > 0)
            {
                MaterialRegistry.INSTANCE.registerTypeIcon(type.name, ores.get(0));
            }
            else
            {
                for (OreDictMaterial material : OreDictMaterial.MATERIALS)
                {
                    ores = OreDictionary.getOres(type.prefix + material.suffix, false);
                    if (ores.size() > 0)
                    {
                        MaterialRegistry.INSTANCE.registerTypeIcon(type.name, ores.get(0));
                        break;
                    }
                }
            }
        }
        ModIntegrationManager.clientPostInit();
    }

    @Override
    public void preInit()
    {
        MaterialRegistry.INSTANCE.initIcons();
        for (Map.Entry<String, FluidLiquidMetal> e : LiquidMetalRegistry.INSTANCE.getFluids().entrySet())
        {
            Fluid fluid = e.getValue();
            Block block = fluid.getBlock();
            Item item = Item.getItemFromBlock(block);
            String name = e.getKey();
            ModelBakery.registerItemVariants(item);
            ModelLoader.setCustomMeshDefinition(item, new LiquidMetalItemMeshDefinition(name));
            ModelLoader.setCustomStateMapper(block, new StateMap.Builder().ignore(BlockFluidBase.LEVEL).build());
        }
        ModIntegrationManager.clientPreInit();
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void registerItemModel(Block block, String name)
    {
        registerItemModel(Item.getItemFromBlock(block), name);
    }

    private void registerItemModel(Block block, String name, int meta)
    {
        registerItemModel(Item.getItemFromBlock(block), name, meta);
    }

    private void registerItemModel(Item item, String name)
    {
        ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation("foundry:" + name, "inventory"));
    }

    private void registerItemModel(Item item, String name, int meta)
    {
        name = "foundry:" + name;
        ModelBakery.registerItemVariants(item, new ResourceLocation(name));
        ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(name, "inventory"));
    }

    @Override
    public String translate(String string, Object... args)
    {
        return I18n.format(string, args);
    }
}

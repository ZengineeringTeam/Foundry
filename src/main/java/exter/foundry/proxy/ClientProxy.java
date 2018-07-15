package exter.foundry.proxy;

import java.util.List;

import exter.foundry.Foundry;
import exter.foundry.integration.ModIntegrationManager;
import exter.foundry.material.MaterialRegistry;
import exter.foundry.material.OreDictMaterial;
import exter.foundry.material.OreDictType;
import exter.foundry.tileentity.TileEntityCastingTableBlock;
import exter.foundry.tileentity.TileEntityCastingTableIngot;
import exter.foundry.tileentity.TileEntityCastingTablePlate;
import exter.foundry.tileentity.TileEntityCastingTableRod;
import exter.foundry.tileentity.renderer.CastingTableRenderer;
import exter.foundry.tileentity.renderer.CastingTableRendererBlock;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.oredict.OreDictionary;

public class ClientProxy extends CommonProxy
{
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
        ModIntegrationManager.clientPreInit();
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
        ModelLoader.setCustomModelResourceLocation(item, 0,
                new ModelResourceLocation(Foundry.MODID + ":" + name, "inventory"));
    }

    private void registerItemModel(Item item, String name, int meta)
    {
        name = Foundry.MODID + ":" + name;
        ModelBakery.registerItemVariants(item, new ResourceLocation(name));
        ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(name, "inventory"));
    }
}

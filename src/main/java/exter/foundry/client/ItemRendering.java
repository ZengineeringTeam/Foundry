package exter.foundry.client;

import exter.foundry.block.BlockCastingTable;
import exter.foundry.block.BlockComponent;
import exter.foundry.block.BlockMachine;
import exter.foundry.block.FoundryBlocks;
import exter.foundry.item.FoundryItems;
import exter.foundry.item.ItemMold;
import exter.foundry.util.ModelUtil;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@EventBusSubscriber(Side.CLIENT)
public class ItemRendering
{
    @SubscribeEvent
    public static void onModelRegister(ModelRegistryEvent event)
    {
        ModelUtil.mapItemModel(FoundryBlocks.block_mold_station);
        ModelUtil.mapItemModel(FoundryBlocks.block_burner_heater);
        ModelUtil.mapItemModel(FoundryBlocks.block_cauldron_bronze);

        ModelUtil.mapItemVariantsModel(FoundryBlocks.block_machine, "",
                BlockMachine.EnumMachine.values());
        ModelUtil.mapItemVariantsModel(FoundryBlocks.block_casting_table, "_table",
                BlockCastingTable.EnumTable.values());
        ModelUtil.mapItemVariantsModel(FoundryBlocks.block_component, "",
                BlockComponent.EnumVariant.values());

        ModelUtil.mapItemModel(FoundryItems.item_small_clay, "small_clay");
        ModelUtil.mapItemVariantsModel(FoundryItems.item_mold, "_mold", ItemMold.SubItem.values());
    }
}

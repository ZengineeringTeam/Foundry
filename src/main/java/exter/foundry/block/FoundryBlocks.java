package exter.foundry.block;

import exter.foundry.item.ItemBlockMulti;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

@EventBusSubscriber
public class FoundryBlocks
{
    static public BlockComponent block_component;
    static public BlockMachine block_machine;
    static public BlockCastingTable block_casting_table;

    static public BlockMoldStation block_mold_station;
    static public BlockBurnerHeater block_burner_heater;

    static public BlockCauldronBronze block_cauldron_bronze;

    @SubscribeEvent
    public static void registerBlocks(Register<Block> event)
    {
        block_component = new BlockComponent();
        block_machine = new BlockMachine();
        block_casting_table = new BlockCastingTable();

        block_mold_station = new BlockMoldStation();
        block_burner_heater = new BlockBurnerHeater();
        block_cauldron_bronze = new BlockCauldronBronze();

        event.getRegistry().registerAll(block_component, block_machine, block_casting_table, block_mold_station,
                block_burner_heater, block_cauldron_bronze);
    }

    @SubscribeEvent
    public static void registerItems(Register<Item> event)
    {
        IForgeRegistry<Item> registry = event.getRegistry();
        registerItem(registry, block_component);
        registerItem(registry, block_machine);
        registerItem(registry, block_casting_table);
        registerItem(registry, block_mold_station);
        registerItem(registry, block_burner_heater);
        registerItem(registry, block_cauldron_bronze);
    }

    private static void registerItem(IForgeRegistry<Item> registry, Block block)
    {
        registry.register((block instanceof IBlockVariants ? new ItemBlockMulti(block) : new ItemBlock(block))
                .setRegistryName(block.getRegistryName()));
    }
}

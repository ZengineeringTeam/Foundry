package exter.foundry.block;

import exter.foundry.FoundryRegistry;
import exter.foundry.item.ItemBlockMulti;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.common.config.Configuration;

public class FoundryBlocks
{
    static public BlockComponent block_component;
    static public BlockFoundryMachine block_machine;
    static public BlockCastingTable block_casting_table;

    static public BlockMoldStation block_mold_station;
    static public BlockBurnerHeater block_burner_heater;

    static public BlockCauldronBronze block_cauldron_bronze;

    static public void register(Block block)
    {
        FoundryRegistry.BLOCKS.add(block);
        FoundryRegistry.ITEMS.add(new ItemBlock(block).setRegistryName(block.getRegistryName()));
    }

    static public void registerBlocks(Configuration config)
    {
        block_component = new BlockComponent();
        block_machine = new BlockFoundryMachine();
        block_casting_table = new BlockCastingTable();

        block_mold_station = new BlockMoldStation();
        block_burner_heater = new BlockBurnerHeater();
        block_cauldron_bronze = new BlockCauldronBronze();

        registerMulti(block_component);
        registerMulti(block_machine);
        registerMulti(block_casting_table);
        register(block_mold_station);
        register(block_burner_heater);
        register(block_cauldron_bronze);
    }

    static private <T extends Block & IBlockVariants> void registerMulti(T block)
    {
        FoundryRegistry.BLOCKS.add(block);
        FoundryRegistry.ITEMS.add(new ItemBlockMulti(block).setRegistryName(block.getRegistryName()));
    }
}

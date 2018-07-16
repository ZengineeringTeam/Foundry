package exter.foundry.item;

import exter.foundry.Foundry;
import exter.foundry.creativetab.FoundryTab;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@EventBusSubscriber
public class FoundryItems
{
    static public Item item_small_clay;
    static public ItemMold item_mold;

    @SubscribeEvent
    public static void registerItems(Register<Item> event)
    {
        event.getRegistry().register(item_small_clay = new Item().setRegistryName(Foundry.MODID, "small_clay")
                .setUnlocalizedName(Foundry.MODID + ".small_clay").setCreativeTab(FoundryTab.INSTANCE));
        event.getRegistry().register(item_mold = new ItemMold());
    }
}

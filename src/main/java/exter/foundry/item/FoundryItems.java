package exter.foundry.item;

import exter.foundry.Foundry;
import exter.foundry.FoundryRegistry;
import exter.foundry.creativetab.FoundryTab;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.oredict.OreDictionary;

@EventBusSubscriber
public class FoundryItems
{
    static public Item item_small_clay;
    static public ItemMold item_mold;

    @SubscribeEvent
    public static void registerItems(Register<Item> event)
    {
        event.getRegistry().register(item_small_clay = new Item().setRegistryName(Foundry.MODID, "small_clay")
                .setTranslationKey(Foundry.MODID + ".small_clay").setCreativeTab(FoundryTab.INSTANCE));
        event.getRegistry().register(item_mold = new ItemMold());

        Item clay = ForgeRegistries.ITEMS.getValue(new ResourceLocation("ceramics:clay_soft"));
        if (clay != null)
        {
            OreDictionary.registerOre(FoundryRegistry.CLAYBLOCK, clay);
        }
        Item barrel = ForgeRegistries.ITEMS.getValue(new ResourceLocation("ceramics:porcelain_barrel"));
        if (barrel != null)
        {
            OreDictionary.registerOre(FoundryRegistry.BARREL, barrel);
        }

    }
}

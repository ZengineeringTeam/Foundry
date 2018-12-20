package exter.foundry.api.material;

import java.util.Set;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Registry for the Material Router.
 */
public interface IMaterialRegistry
{
    /**
     * Get the material the item is made of. Ex: "Iron"/"Gold".
     */
    String getMaterial(ItemStack item);

    /**
     * Get the item stack used for the material icon in the Material Rounter's GUI..
     */
    @SideOnly(Side.CLIENT)
    ItemStack getMaterialIcon(String material);

    /**
     * Get all registered material names.
     */
    Set<String> getMaterialNames();

    /**
     * Get what type item is. Ex: "Ingot"/"Dust".
     */
    String getType(ItemStack item);

    /**
     * Get the item stack used for the type icon in the Material Rounter's GUI..
     */
    @SideOnly(Side.CLIENT)
    ItemStack getTypeIcon(String type);

    /**
     * Get all registered type names.
     */
    Set<String> getTypeNames();

    /**
     * Registers an item.
     * @param oredict_name Ore Dictionary name to register.
     * @param material Material name to register. Ex: "Iron".
     * @param type Type name to register. Ex: "Ingot".
     */
    void registerItem(ItemStack item, String material, String type);

    /**
     * Registers all items with in the Ore Dictionary.
     * Note: Any item added to the same Ore Dictionary entry after calling this
     * will not be registered in the Material Registry
     * @param oredict_name Ore Dictionary name to register.
     * @param material Material name to register. Ex: "Iron".
     * @param type Type name to register. Ex: "Ingot".
     */
    void registerItem(String oredict_name, String material, String type);

    /**
     * Register an icon for a material in the Material Rounter's GUI..
     * @param material The material name to register.
     * @param icon Item stack the icon is taken from.
     */
    @SideOnly(Side.CLIENT)
    void registerMaterialIcon(String material, ItemStack icon);

    /**
     * Register an icon for a type in the Material Rounter's GUI..
     * @param type The type name to register.
     * @param icon Item stack the icon is taken from.
     */
    @SideOnly(Side.CLIENT)
    void registerTypeIcon(String type, ItemStack stack);
}

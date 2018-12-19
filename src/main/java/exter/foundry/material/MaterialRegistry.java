package exter.foundry.material;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import exter.foundry.api.material.IMaterialRegistry;
import exter.foundry.util.hashstack.HashableItem;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

public final class MaterialRegistry implements IMaterialRegistry
{

    public static final MaterialRegistry INSTANCE = new MaterialRegistry();
    private final HashMap<HashableItem, String> materials;

    private final HashMap<HashableItem, String> types;
    private final Set<String> material_names;

    private final Set<String> type_names;

    private Map<String, ItemStack> material_icons;
    private Map<String, ItemStack> type_icons;

    private MaterialRegistry()
    {
        materials = new HashMap<>();
        types = new HashMap<>();
        material_names = new HashSet<>();
        type_names = new HashSet<>();
    }

    @Override
    public String getMaterial(ItemStack item)
    {
        return HashableItem.getFromMap(materials, item);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ItemStack getMaterialIcon(String material)
    {
        return material_icons.getOrDefault(material, ItemStack.EMPTY);
    }

    @Override
    public Set<String> getMaterialNames()
    {
        return Collections.unmodifiableSet(material_names);
    }

    @Override
    public String getType(ItemStack item)
    {
        return HashableItem.getFromMap(types, item);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ItemStack getTypeIcon(String type)
    {
        return type_icons.get(type);
    }

    @Override
    public Set<String> getTypeNames()
    {
        return Collections.unmodifiableSet(type_names);
    }

    public void initIcons()
    {
        material_icons = new HashMap<>();
        type_icons = new HashMap<>();
    }

    @Override
    public void registerItem(ItemStack item, String material, String type)
    {
        HashableItem hs = new HashableItem(item);
        materials.put(hs, material);
        types.put(hs, type);
        material_names.add(material);
        type_names.add(type);
    }

    @Override
    public void registerItem(String oredict_name, String material, String type)
    {
        for (ItemStack item : OreDictionary.getOres(oredict_name, false))
        {
            registerItem(item, material, type);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerMaterialIcon(String material, ItemStack icon)
    {
        material_icons.put(material, icon);

    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerTypeIcon(String type, ItemStack icon)
    {
        type_icons.put(type, icon);
    }

}

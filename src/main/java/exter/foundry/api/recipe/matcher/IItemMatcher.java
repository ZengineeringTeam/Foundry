package exter.foundry.api.recipe.matcher;

import java.util.List;

import com.google.common.base.Predicate;

import net.minecraft.item.ItemStack;

public interface IItemMatcher extends Predicate<ItemStack>
{
    int getAmount();

    ItemStack getItem();

    List<ItemStack> getItems();
}

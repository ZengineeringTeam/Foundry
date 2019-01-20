package exter.foundry.integration.jei;

import java.awt.Color;

import exter.foundry.Foundry;
import exter.foundry.api.recipe.IFluidHeaterFuel;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IDrawableAnimated;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.gui.IGuiFluidStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;

public class FluidHeaterJEI
{
    public static class Category implements IRecipeCategory<Wrapper>
    {
        private final IDrawableStatic background;
        private final String localizedName;

        public Category(IGuiHelper guiHelper)
        {
            background = guiHelper.drawableBuilder(new ResourceLocation("jei", "textures/gui/gui_vanilla.png"), 0, 134, 18, 34).addPadding(0, 0, 0, 88).build();
            localizedName = I18n.format("gui.jei." + getUid());
        }

        @Override
        public String getUid()
        {
            return FoundryJEIConstants.FLUID_HEATER_UID;
        }

        @Override
        public String getTitle()
        {
            return localizedName;
        }

        @Override
        public String getModName()
        {
            return Foundry.MODID;
        }

        @Override
        public IDrawable getBackground()
        {
            return background;
        }

        @Override
        public void setRecipe(IRecipeLayout recipeLayout, Wrapper recipeWrapper, IIngredients ingredients)
        {
            IGuiFluidStackGroup guiFluids = recipeLayout.getFluidStacks();

            guiFluids.init(0, true, 1, 17, 16, 16, 1, false, null);
            guiFluids.set(ingredients);
        }

    }

    public static class Wrapper implements IRecipeWrapper
    {
        private final IFluidHeaterFuel fuel;

        public Wrapper(IGuiHelper guiHelper, IFluidHeaterFuel fuel)
        {
            this.fuel = fuel;
        }

        @Override
        public void getIngredients(IIngredients ingredients)
        {
            ingredients.setInput(VanillaTypes.FLUID, new FluidStack(fuel.getFluid(), 1));
        }

        @Override
        public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY)
        {
            if (JEIFoundryPlugin.flame != null)
            {
                JEIFoundryPlugin.flame.draw(minecraft, 2, 0);
            }
            minecraft.fontRenderer.drawString(fuel.getHeat() / 100 + "K 1Tick", 44, 13, Color.gray.getRGB());
        }

    }
}

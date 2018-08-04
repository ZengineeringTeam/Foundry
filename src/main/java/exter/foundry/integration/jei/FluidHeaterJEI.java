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
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.config.Constants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fluids.FluidStack;

public class FluidHeaterJEI
{
    public static class Category implements IRecipeCategory<Wrapper>
    {
        private final IDrawableStatic background;
        private final IDrawableStatic flameTransparentBackground;
        private final String localizedName;

        public Category(IGuiHelper guiHelper)
        {
            background = guiHelper.drawableBuilder(Constants.RECIPE_GUI_VANILLA, 0, 134, 18, 34).addPadding(0, 0, 0, 88)
                    .build();

            flameTransparentBackground = guiHelper.createDrawable(Constants.RECIPE_BACKGROUND, 215, 0, 14, 14);
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
        private final IDrawableAnimated flame;

        public Wrapper(IGuiHelper guiHelper, IFluidHeaterFuel fuel)
        {
            this.fuel = fuel;
            flame = guiHelper.drawableBuilder(Constants.RECIPE_GUI_VANILLA, 82, 114, 14, 14).buildAnimated(100,
                    IDrawableAnimated.StartDirection.TOP, true);
        }

        @Override
        public void getIngredients(IIngredients ingredients)
        {
            ingredients.setInput(FluidStack.class, new FluidStack(fuel.getFluid(), 1));
        }

        @Override
        public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY)
        {
            flame.draw(minecraft, 2, 0);
            minecraft.fontRenderer.drawString(fuel.getHeat() / 100 + "K 1Tick", 44, 13, Color.gray.getRGB());
        }

    }
}

package exter.foundry.integration.jei;

import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

import exter.foundry.Foundry;
import exter.foundry.api.FoundryAPI;
import exter.foundry.api.recipe.IMeltingRecipe;
import exter.foundry.client.gui.GuiMeltingCrucible;
import exter.foundry.tileentity.TileEntityHeatable;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IDrawableAnimated;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.gui.IGuiFluidStackGroup;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.gui.elements.DrawableResource;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;

public class MeltingJEI
{

    static public class Category implements IRecipeCategory<Wrapper>
    {

        protected final ResourceLocation backgroundLocation;
        @Nonnull
        protected final IDrawableAnimated arrow;
        @Nonnull
        private final IDrawable background;
        @Nonnull
        private final String localizedName;
        @Nonnull
        private final IDrawable tank_overlay;

        public Category(IJeiHelpers helpers)
        {
            IGuiHelper guiHelper = helpers.getGuiHelper();
            backgroundLocation = new ResourceLocation(Foundry.MODID, "textures/gui/crucible.png");

            IDrawableStatic arrowDrawable = guiHelper.createDrawable(backgroundLocation, 176, 78, 24, 17);
            arrow = guiHelper.createAnimatedDrawable(arrowDrawable, 200, IDrawableAnimated.StartDirection.LEFT, false);

            ResourceLocation location = new ResourceLocation(Foundry.MODID, "textures/gui/crucible.png");
            background = guiHelper.createDrawable(location, 30, 16, 94, 54);
            tank_overlay = guiHelper.createDrawable(location, 176, 0, 16, 47);
            localizedName = I18n.format("gui.jei." + getUid());

        }

        @Override
        public void drawExtras(Minecraft minecraft)
        {
            arrow.draw(minecraft, 49, 7);
        }

        @Override
        @Nonnull
        public IDrawable getBackground()
        {
            return background;
        }

        @Override
        public IDrawable getIcon()
        {
            return null;
        }

        @Override
        public String getModName()
        {
            return Foundry.MODID;
        }

        @Override
        public String getTitle()
        {
            return localizedName;
        }

        @Override
        public List<String> getTooltipStrings(int mouseX, int mouseY)
        {
            return Collections.emptyList();
        }

        @Nonnull
        @Override
        public String getUid()
        {
            return FoundryJEIConstants.MELT_UID;
        }

        @Override
        public void setRecipe(IRecipeLayout recipeLayout, Wrapper recipeWrapper, IIngredients ingredients)
        {
            IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
            IGuiFluidStackGroup guiFluidStacks = recipeLayout.getFluidStacks();

            guiItemStacks.init(0, true, 24, 6);
            guiFluidStacks.init(1, false, 77, 6, 16, GuiMeltingCrucible.TANK_HEIGHT, FoundryAPI.CRUCIBLE_TANK_CAPACITY,
                    false, tank_overlay);
            guiItemStacks.set(0, ingredients.getInputs(ItemStack.class).get(0));
            guiFluidStacks.set(1, ingredients.getOutputs(FluidStack.class).get(0));
        }
    }

    public static class Wrapper implements IRecipeWrapper
    {

        private final IDrawable temp;

        private final IMeltingRecipe recipe;

        public Wrapper(IMeltingRecipe recipe)
        {
            this.recipe = recipe;
            ResourceLocation background_location = new ResourceLocation(Foundry.MODID, "textures/gui/crucible.png");
            temp = new DrawableResource(background_location, 176, 53,
                    (recipe.getMeltingPoint() * 100 - TileEntityHeatable.TEMP_MIN) * 54
                            / (500000 - TileEntityHeatable.TEMP_MIN),
                    12, 0, 0, 0, 0, 256, 256);
        }

        @Override
        public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY)
        {
            if (temp != null)
            {
                temp.draw(minecraft, 11, 41);
            }

            minecraft.fontRenderer.drawString(I18n.format("gui.jei.foundry.melting.point", recipe.getMeltingPoint()),
                    14, 28, 0);
        }

        @Override
        public boolean equals(Object other)
        {
            return recipe == other;
        }

        @Override
        public void getIngredients(IIngredients ingredients)
        {
            ingredients.setInputLists(ItemStack.class, Collections.singletonList(recipe.getInput().getItems()));
            ingredients.setOutput(FluidStack.class, recipe.getOutput());
        }

        @Override
        public List<String> getTooltipStrings(int mouseX, int mouseY)
        {
            return null;
        }

        @Override
        public boolean handleClick(Minecraft minecraft, int mouseX, int mouseY, int mouseButton)
        {
            return false;
        }
    }
}

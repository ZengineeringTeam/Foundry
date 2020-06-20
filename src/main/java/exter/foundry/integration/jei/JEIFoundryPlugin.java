package exter.foundry.integration.jei;

import exter.foundry.api.recipe.IAlloyMixerRecipe;
import exter.foundry.api.recipe.IAlloyingCrucibleRecipe;
import exter.foundry.api.recipe.ICastingRecipe;
import exter.foundry.api.recipe.ICastingTableRecipe;
import exter.foundry.api.recipe.IInfuserRecipe;
import exter.foundry.api.recipe.IMeltingRecipe;
import exter.foundry.api.recipe.IMoldRecipe;
import exter.foundry.block.BlockCastingTable;
import exter.foundry.block.BlockMachine;
import exter.foundry.block.FoundryBlocks;
import exter.foundry.client.gui.GuiAlloyMixer;
import exter.foundry.client.gui.GuiAlloyingCrucible;
import exter.foundry.client.gui.GuiMeltingCrucible;
import exter.foundry.client.gui.GuiMetalCaster;
import exter.foundry.client.gui.GuiMetalInfuser;
import exter.foundry.client.gui.GuiMoldStation;
import exter.foundry.container.ContainerMeltingCrucible;
import exter.foundry.container.ContainerMetalCaster;
import exter.foundry.container.ContainerMetalInfuser;
import exter.foundry.recipes.manager.AlloyMixerRecipeManager;
import exter.foundry.recipes.manager.AlloyingCrucibleRecipeManager;
import exter.foundry.recipes.manager.CastingRecipeManager;
import exter.foundry.recipes.manager.CastingTableRecipeManager;
import exter.foundry.recipes.manager.FluidHeaterFuelManager;
import exter.foundry.recipes.manager.InfuserRecipeManager;
import exter.foundry.recipes.manager.MeltingRecipeManager;
import exter.foundry.recipes.manager.MoldRecipeManager;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.gui.IDrawableAnimated;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import mezz.jei.api.recipe.transfer.IRecipeTransferRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;

@JEIPlugin
public class JEIFoundryPlugin implements IModPlugin
{

    CastingTableJEI table_ingot;
    CastingTableJEI table_plate;
    CastingTableJEI table_rod;
    CastingTableJEI table_block;
    static IDrawableAnimated flame;

    @Override
    public void register(IModRegistry registry)
    {

        IRecipeTransferRegistry transferRegistry = registry.getRecipeTransferRegistry();

        transferRegistry.addRecipeTransferHandler(ContainerMeltingCrucible.class, FoundryJEIConstants.MELT_UID, ContainerMeltingCrucible.SLOTS_TE, ContainerMeltingCrucible.SLOTS_TE_SIZE, ContainerMeltingCrucible.SLOTS_INVENTORY, 36);
        registry.addRecipeClickArea(GuiMeltingCrucible.class, 79, 23, 22, 15, FoundryJEIConstants.MELT_UID);
        registry.addRecipeCatalyst(FoundryBlocks.block_machine.asItemStack(BlockMachine.EnumMachine.CRUCIBLE_BASIC), FoundryJEIConstants.MELT_UID);
        registry.addRecipeCatalyst(FoundryBlocks.block_machine.asItemStack(BlockMachine.EnumMachine.CRUCIBLE_STANDARD), FoundryJEIConstants.MELT_UID);
        registry.addRecipeCatalyst(FoundryBlocks.block_machine.asItemStack(BlockMachine.EnumMachine.CRUCIBLE_ADVANCED), FoundryJEIConstants.MELT_UID);
        registry.handleRecipes(IMeltingRecipe.class, MeltingJEI.Wrapper::new, FoundryJEIConstants.MELT_UID);
        registry.addRecipes(MeltingRecipeManager.INSTANCE.getRecipes(), FoundryJEIConstants.MELT_UID);

        transferRegistry.addRecipeTransferHandler(ContainerMetalCaster.class, FoundryJEIConstants.CAST_UID, ContainerMetalCaster.SLOTS_TE, ContainerMetalCaster.SLOTS_TE_SIZE, ContainerMetalCaster.SLOTS_INVENTORY, 36);
        registry.addRecipeClickArea(GuiMetalCaster.class, 60, 51, 22, 15, FoundryJEIConstants.CAST_UID);
        registry.addRecipeCatalyst(FoundryBlocks.block_machine.asItemStack(BlockMachine.EnumMachine.CASTER), FoundryJEIConstants.CAST_UID);
        registry.handleRecipes(ICastingRecipe.class, CastingJEI.Wrapper::new, FoundryJEIConstants.CAST_UID);
        registry.addRecipes(CastingRecipeManager.INSTANCE.getRecipes(), FoundryJEIConstants.CAST_UID);

        transferRegistry.addRecipeTransferHandler(ContainerMetalInfuser.class, FoundryJEIConstants.INF_UID, ContainerMetalInfuser.SLOTS_TE, ContainerMetalInfuser.SLOTS_TE_SIZE, ContainerMetalInfuser.SLOTS_INVENTORY, 36);
        registry.addRecipeClickArea(GuiMetalInfuser.class, 49, 59, 22, 15, FoundryJEIConstants.INF_UID);
        registry.addRecipeClickArea(GuiMetalInfuser.class, 96, 59, 22, 15, FoundryJEIConstants.INF_UID);
        registry.addRecipeCatalyst(FoundryBlocks.block_machine.asItemStack(BlockMachine.EnumMachine.INFUSER), FoundryJEIConstants.INF_UID);
        registry.handleRecipes(IInfuserRecipe.class, InfuserJEI.Wrapper::new, FoundryJEIConstants.INF_UID);
        registry.addRecipes(InfuserRecipeManager.INSTANCE.getRecipes(), FoundryJEIConstants.INF_UID);

        registry.addRecipeCatalyst(new ItemStack(FoundryBlocks.block_mold_station), FoundryJEIConstants.MOLD_UID);
        registry.addRecipeClickArea(GuiMoldStation.class, 117, 39, 22, 15, FoundryJEIConstants.MOLD_UID);
        registry.handleRecipes(IMoldRecipe.class, MoldStationJEI.Wrapper::new, FoundryJEIConstants.MOLD_UID);
        registry.addRecipes(MoldRecipeManager.INSTANCE.getRecipes(), FoundryJEIConstants.MOLD_UID);

        registry.addRecipeCatalyst(FoundryBlocks.block_machine.asItemStack(BlockMachine.EnumMachine.ALLOY_MIXER), FoundryJEIConstants.AM_UID);
        registry.addRecipeClickArea(GuiAlloyMixer.class, 108, 55, 22, 15, FoundryJEIConstants.AM_UID);
        registry.handleRecipes(IAlloyMixerRecipe.class, AlloyMixerJEI.Wrapper::new, FoundryJEIConstants.AM_UID);
        registry.addRecipes(AlloyMixerRecipeManager.INSTANCE.getRecipes(), FoundryJEIConstants.AM_UID);

        registry.addRecipeCatalyst(FoundryBlocks.block_machine.asItemStack(BlockMachine.EnumMachine.ALLOYING_CRUCIBLE), FoundryJEIConstants.AC_UID);
        registry.addRecipeClickArea(GuiAlloyingCrucible.class, 54, 55, 22, 15, FoundryJEIConstants.AC_UID);
        registry.addRecipeClickArea(GuiAlloyingCrucible.class, 100, 55, 22, 15, FoundryJEIConstants.AC_UID);
        registry.handleRecipes(IAlloyingCrucibleRecipe.class, AlloyingCrucibleJEI.Wrapper::new, FoundryJEIConstants.AC_UID);
        registry.addRecipes(AlloyingCrucibleRecipeManager.INSTANCE.getRecipes(), FoundryJEIConstants.AC_UID);

        registry.addRecipeCatalyst(FoundryBlocks.block_machine.asItemStack(BlockMachine.EnumMachine.FLUID_HEATER), FoundryJEIConstants.FLUID_HEATER_UID);
        NonNullList<FluidHeaterJEI.Wrapper> fuels = NonNullList.create();
        FluidHeaterFuelManager.INSTANCE.getFuels().forEach(f -> fuels.add(new FluidHeaterJEI.Wrapper(registry.getJeiHelpers().getGuiHelper(), f)));
        registry.addRecipes(fuels, FoundryJEIConstants.FLUID_HEATER_UID);

        setupTable(registry, table_ingot);
        setupTable(registry, table_plate);
        setupTable(registry, table_rod);
        setupTable(registry, table_block);
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry)
    {
        table_ingot = new CastingTableJEI(BlockCastingTable.EnumTable.INGOT);
        table_plate = new CastingTableJEI(BlockCastingTable.EnumTable.PLATE);
        table_rod = new CastingTableJEI(BlockCastingTable.EnumTable.ROD);
        table_block = new CastingTableJEI(BlockCastingTable.EnumTable.BLOCK);

        IJeiHelpers helpers = registry.getJeiHelpers();
        flame = helpers.getGuiHelper().drawableBuilder(new ResourceLocation("jei", "textures/gui/gui_vanilla.png"), 82, 114, 14, 14).buildAnimated(100, IDrawableAnimated.StartDirection.TOP, true);

        registry.addRecipeCategories(new MoldStationJEI.Category(helpers), new MeltingJEI.Category(helpers));
        registry.addRecipeCategories(new CastingJEI.Category(helpers));
        registry.addRecipeCategories(new AlloyMixerJEI.Category(helpers));
        registry.addRecipeCategories(new AlloyingCrucibleJEI.Category(helpers));
        registry.addRecipeCategories(new InfuserJEI.Category(helpers));
        registry.addRecipeCategories(new FluidHeaterJEI.Category(helpers.getGuiHelper()));
        registry.addRecipeCategories(table_ingot.new Category(helpers));
        registry.addRecipeCategories(table_plate.new Category(helpers));
        registry.addRecipeCategories(table_rod.new Category(helpers));
        registry.addRecipeCategories(table_block.new Category(helpers));
    }

    private void setupTable(IModRegistry registry, CastingTableJEI table)
    {
        registry.addRecipeCatalyst(FoundryBlocks.block_casting_table.asItemStack(table.getType()), table.getUID());
        registry.handleRecipes(ICastingTableRecipe.class, CastingTableJEI.Wrapper::new, table.getUID());
        registry.addRecipes(CastingTableRecipeManager.INSTANCE.getRecipes(table.getType().type), table.getUID());
    }

}

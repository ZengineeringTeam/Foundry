package exter.foundry.integration.jei;

import exter.foundry.api.recipe.IAlloyMixerRecipe;
import exter.foundry.api.recipe.IAlloyingCrucibleRecipe;
import exter.foundry.api.recipe.ICastingRecipe;
import exter.foundry.api.recipe.ICastingTableRecipe;
import exter.foundry.api.recipe.IInfuserRecipe;
import exter.foundry.api.recipe.IMeltingRecipe;
import exter.foundry.api.recipe.IMoldRecipe;
import exter.foundry.block.BlockCastingTable;
import exter.foundry.block.BlockFoundryMachine;
import exter.foundry.block.FoundryBlocks;
import exter.foundry.container.ContainerMeltingCrucible;
import exter.foundry.container.ContainerMetalCaster;
import exter.foundry.container.ContainerMetalInfuser;
import exter.foundry.recipes.manager.AlloyMixerRecipeManager;
import exter.foundry.recipes.manager.AlloyingCrucibleRecipeManager;
import exter.foundry.recipes.manager.CastingRecipeManager;
import exter.foundry.recipes.manager.CastingTableRecipeManager;
import exter.foundry.recipes.manager.InfuserRecipeManager;
import exter.foundry.recipes.manager.MeltingRecipeManager;
import exter.foundry.recipes.manager.MoldRecipeManager;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import mezz.jei.api.recipe.transfer.IRecipeTransferRegistry;
import net.minecraft.item.ItemStack;

@JEIPlugin
public class JEIFoundryPlugin implements IModPlugin
{

    CastingTableJEI table_ingot;
    CastingTableJEI table_plate;
    CastingTableJEI table_rod;
    CastingTableJEI table_block;

    @Override
    public void register(IModRegistry registry)
    {

        IRecipeTransferRegistry transfer_registry = registry.getRecipeTransferRegistry();

        transfer_registry.addRecipeTransferHandler(ContainerMeltingCrucible.class, FoundryJEIConstants.MELT_UID,
                ContainerMeltingCrucible.SLOTS_TE, ContainerMeltingCrucible.SLOTS_TE_SIZE,
                ContainerMeltingCrucible.SLOTS_INVENTORY, 36);
        registry.addRecipeCatalyst(
                FoundryBlocks.block_machine.asItemStack(BlockFoundryMachine.EnumMachine.CRUCIBLE_BASIC),
                FoundryJEIConstants.MELT_UID);
        registry.addRecipeCatalyst(
                FoundryBlocks.block_machine.asItemStack(BlockFoundryMachine.EnumMachine.CRUCIBLE_STANDARD),
                FoundryJEIConstants.MELT_UID);
        registry.addRecipeCatalyst(
                FoundryBlocks.block_machine.asItemStack(BlockFoundryMachine.EnumMachine.CRUCIBLE_ADVANCED),
                FoundryJEIConstants.MELT_UID);
        registry.handleRecipes(IMeltingRecipe.class, MeltingJEI.Wrapper::new, FoundryJEIConstants.MELT_UID);
        registry.addRecipes(MeltingRecipeManager.INSTANCE.getRecipes(), FoundryJEIConstants.MELT_UID);

        transfer_registry.addRecipeTransferHandler(ContainerMetalCaster.class, FoundryJEIConstants.CAST_UID,
                ContainerMetalCaster.SLOTS_TE, ContainerMetalCaster.SLOTS_TE_SIZE, ContainerMetalCaster.SLOTS_INVENTORY,
                36);
        registry.addRecipeCatalyst(FoundryBlocks.block_machine.asItemStack(BlockFoundryMachine.EnumMachine.CASTER),
                FoundryJEIConstants.CAST_UID);
        registry.handleRecipes(ICastingRecipe.class, CastingJEI.Wrapper::new, FoundryJEIConstants.CAST_UID);
        registry.addRecipes(CastingRecipeManager.INSTANCE.getRecipes(), FoundryJEIConstants.CAST_UID);

        transfer_registry.addRecipeTransferHandler(ContainerMetalInfuser.class, FoundryJEIConstants.INF_UID,
                ContainerMetalInfuser.SLOTS_TE, ContainerMetalInfuser.SLOTS_TE_SIZE,
                ContainerMetalInfuser.SLOTS_INVENTORY, 36);
        registry.addRecipeCatalyst(FoundryBlocks.block_machine.asItemStack(BlockFoundryMachine.EnumMachine.INFUSER),
                FoundryJEIConstants.INF_UID);
        registry.handleRecipes(IInfuserRecipe.class, InfuserJEI.Wrapper::new, FoundryJEIConstants.INF_UID);
        registry.addRecipes(InfuserRecipeManager.INSTANCE.getRecipes(), FoundryJEIConstants.INF_UID);

        registry.addRecipeCatalyst(new ItemStack(FoundryBlocks.block_mold_station), FoundryJEIConstants.MOLD_UID);
        registry.handleRecipes(IMoldRecipe.class, MoldStationJEI.Wrapper::new, FoundryJEIConstants.MOLD_UID);
        registry.addRecipes(MoldRecipeManager.INSTANCE.getRecipes(), FoundryJEIConstants.MOLD_UID);

        registry.addRecipeCatalyst(FoundryBlocks.block_machine.asItemStack(BlockFoundryMachine.EnumMachine.ALLOYMIXER),
                FoundryJEIConstants.AM_UID);
        registry.handleRecipes(IAlloyMixerRecipe.class, AlloyMixerJEI.Wrapper::new, FoundryJEIConstants.AM_UID);
        registry.addRecipes(AlloyMixerRecipeManager.INSTANCE.getRecipes(), FoundryJEIConstants.AM_UID);

        registry.addRecipeCatalyst(
                FoundryBlocks.block_machine.asItemStack(BlockFoundryMachine.EnumMachine.ALLOYING_CRUCIBLE),
                FoundryJEIConstants.AC_UID);
        registry.handleRecipes(IAlloyingCrucibleRecipe.class, AlloyingCrucibleJEI.Wrapper::new,
                FoundryJEIConstants.AC_UID);
        registry.addRecipes(AlloyingCrucibleRecipeManager.INSTANCE.getRecipes(), FoundryJEIConstants.AC_UID);

        registry.addRecipeCatalyst(FoundryBlocks.block_casting_table.asItemStack(BlockCastingTable.EnumTable.INGOT),
                "foundry.casting_table.ingot");

        registry.addRecipeCatalyst(FoundryBlocks.block_casting_table.asItemStack(BlockCastingTable.EnumTable.PLATE),
                "foundry.casting_table.plate");

        registry.addRecipeCatalyst(FoundryBlocks.block_casting_table.asItemStack(BlockCastingTable.EnumTable.ROD),
                "foundry.casting_table.rod");

        registry.addRecipeCatalyst(FoundryBlocks.block_casting_table.asItemStack(BlockCastingTable.EnumTable.BLOCK),
                "foundry.casting_table.block");

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
        registry.addRecipeCategories(new MeltingJEI.Category(helpers), new CastingJEI.Category(helpers),
                new AlloyMixerJEI.Category(helpers), new AlloyingCrucibleJEI.Category(helpers),
                new InfuserJEI.Category(helpers), table_ingot.new Category(helpers), table_plate.new Category(helpers),
                table_rod.new Category(helpers), table_block.new Category(helpers));
    }

    private void setupTable(IModRegistry registry, CastingTableJEI table)
    {
        registry.addRecipeCatalyst(FoundryBlocks.block_casting_table.asItemStack(table.getType()), table.getUID());
        registry.handleRecipes(ICastingTableRecipe.class, CastingTableJEI.Wrapper::new, table.getUID());
        registry.addRecipes(CastingTableRecipeManager.INSTANCE.getRecipes(table.getType().type), table.getUID());
    }

}

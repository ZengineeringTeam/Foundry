package exter.foundry.tileentity;

import exter.foundry.api.recipe.IMoldRecipe;
import exter.foundry.block.BlockMoldStation;
import exter.foundry.item.FoundryItems;
import exter.foundry.item.ItemComponent;
import exter.foundry.recipes.manager.MoldRecipeManager;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fml.common.Optional;
import vazkii.botania.api.item.IExoflameHeatable;

@Optional.Interface(iface = "vazkii.botania.api.item.IExoflameHeatable", modid = "Botania")
public class TileEntityMoldStation extends TileEntityFoundry implements IExoflameHeatable {

	public static final int SLOT_BLOCK = 0;
	public static final int SLOT_CLAY = 1;
	public static final int SLOT_OUTPUT = 2;
	public static final int SLOT_FUEL = 3;
	public static final int MIN_DEPTH = 0;
	public static final int MAX_DEPTH = 4;

	private int burn_time;

	private int item_burn_time;

	private int progress;

	private boolean has_block;

	private boolean update_burn_times;

	private final int[] grid;

	private IMoldRecipe current_recipe;

	public TileEntityMoldStation() {
		burn_time = 0;
		item_burn_time = 0;
		progress = 0;
		update_burn_times = false;
		grid = new int[36];
		has_block = false;
		current_recipe = null;
	}

	@Optional.Method(modid = "Botania")
	@Override
	public void boostBurnTime() {
		if (!world.isRemote) {
			burn_time = 200;
			item_burn_time = 199;
			update_burn_times = true;
			markDirty();
		}
	}

	@Optional.Method(modid = "Botania")
	@Override
	public void boostCookTime() {

	}

	/**
	 * Returns true if automation can insert the given item in the given slot from
	 * the given side. Args: Slot, item, side
	 */
	public boolean canInsertItem(int par1, ItemStack par2ItemStack, EnumFacing side) {
		return isItemValidForSlot(par1, par2ItemStack);
	}

	private boolean canOutput(ItemStack output, int slot) {
		ItemStack inv_output = getStackInSlot(slot);
		if (inv_output.isEmpty()) return true;
		return inv_output.isItemEqual(output) && inv_output.getCount() - output.getCount() <= inv_output.getMaxStackSize();
	}

	private boolean canRecipeOutput() {
		ItemStack output = current_recipe.getOutput();
		int clay_amount = getCarvedClayAmount();
		ItemStack slot_clay = getStackInSlot(SLOT_CLAY);

		return canOutput(output, SLOT_OUTPUT) && (slot_clay.isEmpty() || slot_clay.getCount() + clay_amount <= slot_clay.getMaxStackSize());
	}

	@Optional.Method(modid = "Botania")
	@Override
	public boolean canSmelt() {
		return has_block && current_recipe != null && canRecipeOutput();
	}

	public void carve(final NonNullList<Boolean> pattern, int depth) {
		if (world.isRemote && has_block && pattern.size() == grid.length) {
			NBTTagCompound tag = new NBTTagCompound();
			for (int i = 0; i < grid.length; i++)
            {
                if (pattern.get(i) == Boolean.TRUE)
                {
                    int depthNew = MathHelper.clamp(depth + grid[i], MIN_DEPTH, MAX_DEPTH);
                    if (depthNew != grid[i])
                    {
                        grid[i] = depthNew;
                        tag.setInteger("RecipeGrid_" + i, grid[i]);
                    }
                }
            }
			sendToServer(tag);
		}
	}

	private void clearGrid() {
		for (int i = 0; i < 36; i++) {
			if (grid[i] > 0) {
				grid[i] = 0;
				updateValue("RecipeGrid_" + i, grid[i]);
			}
		}
		current_recipe = null;
	}

	@Override
	public void closeInventory(EntityPlayer player) {

	}

	private void doSmelt() {
		if (!canRecipeOutput()) {
			progress = 0;
			return;
		}

		ItemStack output = current_recipe.getOutput();
		int clay = getCarvedClayAmount();
		if (++progress == 200) {
			progress = 0;
			if (getStackInSlot(SLOT_OUTPUT).isEmpty()) {
				setStackInSlot(SLOT_OUTPUT, output.copy());
			} else {
				getStackInSlot(SLOT_OUTPUT).grow(output.getCount());
			}
			if (getStackInSlot(SLOT_CLAY).isEmpty()) {
				setStackInSlot(SLOT_CLAY, FoundryItems.component(ItemComponent.SubItem.REFRACTORYCLAY_SMALL, clay));
			} else {
				getStackInSlot(SLOT_CLAY).grow(clay);
			}
			updateInventoryItem(SLOT_OUTPUT);
			updateInventoryItem(SLOT_CLAY);
			has_block = false;
			updateValue("HasBlock", has_block);
			clearGrid();
		}
	}

	public void fire() {
		if (world.isRemote) {
			NBTTagCompound tag = new NBTTagCompound();
			tag.setBoolean("command_fire", true);
			sendToServer(tag);
		}
	}

	public int getBurningTime() {
		return burn_time;
	}

	@Optional.Method(modid = "Botania")
	@Override
	public int getBurnTime() {
		return burn_time <= 1 ? 0 : burn_time - 1;
	}

	private int getCarvedClayAmount() {
		int amount = 0;
		for (int g : grid) {
			amount += g;
		}
		amount /= 10;
		return amount;
	}

	public int getGridSlot(int slot) {
		return grid[slot];
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	public int getItemBurnTime() {
		return item_burn_time;
	}

	public int getProgress() {
		return progress;
	}

	@Override
	public int getSizeInventory() {
		return 4;
	}

	@Override
	public FluidTank getTank(int slot) {
		return null;
	}

	@Override
	public int getTankCount() {
		return 0;
	}

	public boolean hasBlock() {
		return has_block;
	}

	public boolean isBurning() {
		return burn_time > 0;
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		switch (slot) {
		case SLOT_OUTPUT:
			return false;
		case SLOT_FUEL:
			return TileEntityFurnace.isItemFuel(stack);
		}
		return true;
	}

	@Override
	public boolean isUsableByPlayer(EntityPlayer par1EntityPlayer) {
		return world.getTileEntity(getPos()) != this ? false : par1EntityPlayer.getDistanceSq(getPos()) <= 64.0D;
	}

	public void mend(final NonNullList<Boolean> pattern, int depth) {
		carve(pattern, -depth);
	}

	@Override
	protected void onInitialize() {
		current_recipe = null;
	}

	@Override
	public void openInventory(EntityPlayer player) {

	}

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		if (tag.hasKey("BurnTime")) {
			burn_time = tag.getInteger("BurnTime");
		}
		if (tag.hasKey("CookTime")) {
			progress = tag.getInteger("CookTime");
		}
		if (tag.hasKey("ItemBurnTime")) {
			item_burn_time = tag.getInteger("ItemBurnTime");
		}

		boolean grid_changed = false;
		for (int i = 0; i < 36; i++) {
			if (tag.hasKey("RecipeGrid_" + i)) {
				grid[i] = tag.getInteger("RecipeGrid_" + i);
				grid_changed = true;
			}
		}

		if (tag.hasKey("HasBlock")) {
			has_block = tag.getBoolean("HasBlock");
		}

		if (world != null && !world.isRemote && has_block) {
			if (grid_changed) {
				current_recipe = null;
			}
			if (tag.hasKey("command_fire")) {
				current_recipe = MoldRecipeManager.INSTANCE.findRecipe(grid);
			}
			((BlockMoldStation) getBlockType()).setMachineState(world, getPos(), world.getBlockState(getPos()), burn_time > 0);
		}
	}

	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
		return oldState.getBlock() != newState.getBlock();
	}

	@Override
	protected void updateClient() {

	}

	@Override
	protected void updateServer() {
		int last_burn_time = burn_time;
		int last_progress = progress;
		int last_item_burn_time = item_burn_time;

		if (!has_block) {
			ItemStack block = getStackInSlot(SLOT_BLOCK);
			if (!block.isEmpty()) {
				decrStackSize(SLOT_BLOCK, 1);
				has_block = true;
				updateValue("HasBlock", has_block);
				clearGrid();
			}
		}

		if (burn_time > 0) {
			--burn_time;
		}
		if (has_block && progress >= 0) {
//			if (current_recipe == null) current_recipe = MoldRecipeManager.INSTANCE.findRecipe(this.grid);
			if (burn_time == 0 && current_recipe != null && canRecipeOutput()) {
				item_burn_time = burn_time = TileEntityFurnace.getItemBurnTime(getStackInSlot(SLOT_FUEL));
				if (burn_time > 0) {
					if (!getStackInSlot(SLOT_FUEL).isEmpty()) {
						getStackInSlot(SLOT_FUEL).shrink(1);
						if (getStackInSlot(SLOT_FUEL).getCount() == 0) {
							setStackInSlot(SLOT_FUEL, getStackInSlot(SLOT_FUEL).getItem().getContainerItem(getStackInSlot(SLOT_FUEL)));
						}
						updateInventoryItem(SLOT_FUEL);
					}
				}
			}

			if (burn_time > 0) {
				if (current_recipe != null) {
					doSmelt();
				} else {
					progress = 0;
				}
			} else {
				progress = 0;
			}
		}

		if (last_burn_time != burn_time || update_burn_times) {
			if (last_burn_time * burn_time == 0) {
				((BlockMoldStation) getBlockType()).setMachineState(world, getPos(), world.getBlockState(getPos()), burn_time > 0);
			}
			updateValue("BurnTime", burn_time);
		}

		if (last_item_burn_time != item_burn_time || update_burn_times) {
			updateValue("ItemBurnTime", item_burn_time);
		}
		update_burn_times = false;

		if (last_progress != progress) {
			updateValue("CookTime", progress);
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		if (compound == null) {
			compound = new NBTTagCompound();
		}
		super.writeToNBT(compound);
		compound.setInteger("BurnTime", burn_time);
		compound.setInteger("CookTime", progress);
		compound.setInteger("ItemBurnTime", item_burn_time);
		for (int i = 0; i < 36; i++) {
			compound.setInteger("RecipeGrid_" + i, grid[i]);
		}
		compound.setBoolean("HasBlock", has_block);
		return compound;
	}
}

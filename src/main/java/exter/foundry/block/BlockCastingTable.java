package exter.foundry.block;

import java.util.List;
import java.util.Random;

import exter.foundry.Foundry;
import exter.foundry.api.recipe.ICastingTableRecipe.TableType;
import exter.foundry.creativetab.FoundryTab;
import exter.foundry.tileentity.TileEntityCastingTableBase;
import exter.foundry.tileentity.TileEntityCastingTableBlock;
import exter.foundry.tileentity.TileEntityCastingTableIngot;
import exter.foundry.tileentity.TileEntityCastingTablePlate;
import exter.foundry.tileentity.TileEntityCastingTableRod;
import exter.foundry.tileentity.TileEntityFoundry;
import exter.foundry.util.MiscUtil;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockCastingTable extends Block implements ITileEntityProvider, IBlockVariants
{
    static public enum EnumTable implements IStringSerializable
    {
        INGOT(0, "ingot", 9, TableType.INGOT),
        PLATE(1, "plate", 11, TableType.PLATE),
        ROD(2, "rod", 10, TableType.ROD),
        BLOCK(3, "block", 2, TableType.BLOCK);

        static public EnumTable fromID(int num)
        {
            for (EnumTable m : values())
            {
                if (m.id == num)
                {
                    return m;
                }
            }
            return null;
        }

        public final int id;
        public final String name;
        public final int depth;

        public final TableType type;

        private EnumTable(int id, String name, int depth, TableType type)
        {
            this.id = id;
            this.name = name;
            this.depth = depth;
            this.type = type;
        }

        @Override
        public String getName()
        {
            return name;
        }

        @Override
        public String toString()
        {
            return getName();
        }
    }

    protected static final AxisAlignedBB AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.75D, 1.0D);
    protected static final AxisAlignedBB AABB_BLOCK = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.875D, 1.0D);

    public static final PropertyEnum<EnumTable> TABLE = PropertyEnum.create("type", EnumTable.class);

    private final Random rand = new Random();

    public BlockCastingTable()
    {
        super(Material.IRON);
        setHardness(1.0F);
        setResistance(8.0F);
        setSoundType(SoundType.STONE);
        setUnlocalizedName(Foundry.MODID + ".casting_table");
        setCreativeTab(FoundryTab.INSTANCE);
        setRegistryName(Foundry.MODID, "casting_table");
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, World player, List<String> tooltip, ITooltipFlag advanced)
    {
        MiscUtil.localizeTooltip(getUnlocalizedName(stack.getMetadata()) + ".tooltip", tooltip);
    }

    public ItemStack asItemStack(EnumTable machine)
    {
        return new ItemStack(this, 1, getMetaFromState(getDefaultState().withProperty(TABLE, machine)));
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state)
    {
        TileEntity te = world.getTileEntity(pos);

        if (te != null && te instanceof TileEntityCastingTableBase && !world.isRemote)
        {
            TileEntityCastingTableBase tef = (TileEntityCastingTableBase) te;
            if (tef.getProgress() == 0)
            {
                ItemStack is = tef.getStackInSlot(0);

                if (!is.isEmpty())
                {
                    double drop_x = rand.nextFloat() * 0.3 + 0.35;
                    double drop_y = rand.nextFloat() * 0.3 + 0.35;
                    double drop_z = rand.nextFloat() * 0.3 + 0.35;
                    EntityItem entityitem = new EntityItem(world, pos.getX() + drop_x, pos.getY() + drop_y,
                            pos.getZ() + drop_z, is);
                    entityitem.setPickupDelay(10);

                    world.spawnEntity(entityitem);
                }
            }
        }
        world.removeTileEntity(pos);
        super.breakBlock(world, pos, state);
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, TABLE);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta)
    {
        return createTileEntity(world, getStateFromMeta(meta));
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state)
    {
        switch (state.getValue(TABLE))
        {
        case INGOT:
            return new TileEntityCastingTableIngot();
        case PLATE:
            return new TileEntityCastingTablePlate();
        case ROD:
            return new TileEntityCastingTableRod();
        case BLOCK:
            return new TileEntityCastingTableBlock();
        }
        return null;
    }

    @Override
    public int damageDropped(IBlockState state)
    {
        return getMetaFromState(state);
    }

    @Override
    public boolean doesSideBlockRendering(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing face)
    {
        return face == EnumFacing.DOWN;
    }

    private void dropCastingTableOutput(EntityPlayer player, World world, BlockPos pos, IBlockState state)
    {
        TileEntity te = world.getTileEntity(pos);

        if (te != null && te instanceof TileEntityCastingTableBase && !world.isRemote)
        {
            TileEntityCastingTableBase te_ct = (TileEntityCastingTableBase) te;
            if (te_ct.getProgress() == 0)
            {
                ItemStack is = te_ct.getStackInSlot(0);

                if (!is.isEmpty())
                {
                    EntityItem entityitem = new EntityItem(world, pos.getX() + 0.5, pos.getY() + 0.9375,
                            pos.getZ() + 0.5, is);
                    entityitem.setPickupDelay(1);

                    world.spawnEntity(entityitem);
                    te_ct.setInventorySlotContents(0, ItemStack.EMPTY);
                }
            }
        }
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return state.getValue(TABLE).id;
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return getDefaultState().withProperty(TABLE, EnumTable.fromID(meta));
    }

    @Override
    public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> list)
    {
        for (EnumTable m : EnumTable.values())
        {
            list.add(new ItemStack(this, 1, m.id));
        }
    }

    @Override
    public String getUnlocalizedName(int meta)
    {
        return getUnlocalizedName() + "." + getStateFromMeta(meta).getValue(TABLE).name;
    }

    @Override
    public boolean hasTileEntity(IBlockState state)
    {
        return true;
    }

    @Override
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    @Override
    public boolean isTopSolid(IBlockState state)
    {
        return false;
    }

    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos fromPos)
    {
        TileEntityFoundry te = (TileEntityFoundry) world.getTileEntity(pos);

        if (te != null)
        {
            te.updateRedstone();
        }
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hit_x, float hit_y, float hit_z)
    {
        if (world.isRemote)
        {
            return true;
        }
        else
        {
            dropCastingTableOutput(player, world, pos, state);
            return false;
        }
    }
}

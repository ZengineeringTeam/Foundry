package exter.foundry.block;

import java.util.List;

import exter.foundry.Foundry;
import exter.foundry.creativetab.FoundryTab;
import exter.foundry.proxy.CommonProxy;
import exter.foundry.tileentity.TileEntityAlloyMixer;
import exter.foundry.tileentity.TileEntityAlloyingCrucible;
import exter.foundry.tileentity.TileEntityFluidHeater;
import exter.foundry.tileentity.TileEntityFoundry;
import exter.foundry.tileentity.TileEntityInductionHeater;
import exter.foundry.tileentity.TileEntityMaterialRouter;
import exter.foundry.tileentity.TileEntityMeltingCrucibleAdvanced;
import exter.foundry.tileentity.TileEntityMeltingCrucibleBasic;
import exter.foundry.tileentity.TileEntityMeltingCrucibleStandard;
import exter.foundry.tileentity.TileEntityMetalCaster;
import exter.foundry.tileentity.TileEntityMetalInfuser;
import exter.foundry.tileentity.TileEntityPowered;
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
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockMachine extends Block implements ITileEntityProvider, IBlockVariants
{
    static public enum EnumMachine implements IStringSerializable
    {
        CRUCIBLE_BASIC(0, "crucible_basic"),
        CASTER(1, "caster"),
        ALLOY_MIXER(2, "alloy_mixer"),
        INFUSER(3, "infuser"),
        MATERIAL_ROUTER(4, "router"),
        INDUCTION_HEATER(5, "heater_induction"),
        CRUCIBLE_ADVANCED(6, "crucible_advanced"),
        CRUCIBLE_STANDARD(7, "crucible_standard"),
        ALLOYING_CRUCIBLE(8, "alloying_crucible"),
        FLUID_HEATER(9, "fluid_heater");

        static public EnumMachine fromID(int num)
        {
            for (EnumMachine m : values())
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
        private String tooltip;

        private EnumMachine(int id, String name)
        {
            this.id = id;
            this.name = name;
            this.tooltip = name;
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

        public String getTooltipKey()
        {
            return tooltip;
        }

        public void setTooltip(String tooltip)
        {
            this.tooltip = tooltip;
        }
    }

    public static final PropertyEnum<EnumMachine> MACHINE = PropertyEnum.create("machine", EnumMachine.class);

    public BlockMachine()
    {
        super(Material.IRON);
        setHardness(1.0F);
        setResistance(8.0F);
        setSoundType(SoundType.STONE);
        setUnlocalizedName("foundry.machine");
        setCreativeTab(FoundryTab.INSTANCE);
        setRegistryName("machine");
    }

    public ItemStack asItemStack(EnumMachine machine)
    {
        return new ItemStack(this, 1, getMetaFromState(getDefaultState().withProperty(MACHINE, machine)));
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state)
    {
        TileEntity te = world.getTileEntity(pos);

        if (te != null && te instanceof TileEntityFoundry && !world.isRemote)
        {
            TileEntityFoundry tef = (TileEntityFoundry) te;
            int i;
            for (i = 0; i < tef.getSizeInventory(); i++)
            {
                ItemStack is = tef.getStackInSlot(i);

                if (!is.isEmpty())
                {
                    double drop_x = RANDOM.nextFloat() * 0.3 + 0.35;
                    double drop_y = RANDOM.nextFloat() * 0.3 + 0.35;
                    double drop_z = RANDOM.nextFloat() * 0.3 + 0.35;
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
        return new BlockStateContainer(this, MACHINE);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta)
    {
        return createTileEntity(world, getStateFromMeta(meta));
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state)
    {
        switch (state.getValue(MACHINE))
        {
        case CRUCIBLE_BASIC:
            return new TileEntityMeltingCrucibleBasic();
        case CASTER:
            return new TileEntityMetalCaster();
        case ALLOY_MIXER:
            return new TileEntityAlloyMixer();
        case INFUSER:
            return new TileEntityMetalInfuser();
        case MATERIAL_ROUTER:
            return new TileEntityMaterialRouter();
        case INDUCTION_HEATER:
            return new TileEntityInductionHeater();
        case CRUCIBLE_ADVANCED:
            return new TileEntityMeltingCrucibleAdvanced();
        case CRUCIBLE_STANDARD:
            return new TileEntityMeltingCrucibleStandard();
        case ALLOYING_CRUCIBLE:
            return new TileEntityAlloyingCrucible();
        case FLUID_HEATER:
            return new TileEntityFluidHeater();
        }
        return null;
    }

    @Override
    public int damageDropped(IBlockState state)
    {
        return getMetaFromState(state);
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return state.getValue(MACHINE).id;
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return getDefaultState().withProperty(MACHINE, EnumMachine.fromID(meta));
    }

    @Override
    public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> list)
    {
        for (EnumMachine m : EnumMachine.values())
        {
            list.add(new ItemStack(this, 1, m.id));
        }
    }

    @Override
    public String getUnlocalizedName(int meta)
    {
        return "tile." + Foundry.MODID + "." + getStateFromMeta(meta).getValue(MACHINE).name;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, World player, List<String> tooltip, ITooltipFlag advanced)
    {
        MiscUtil.localizeTooltip(getUnlocalizedName(stack.getMetadata()) + ".tooltip", tooltip);
    }

    @Override
    public boolean hasTileEntity(IBlockState state)
    {
        return true;
    }

    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos fromPos)
    {
        TileEntity te = world.getTileEntity(pos);

        if (te instanceof TileEntityFoundry)
        {
            ((TileEntityFoundry) te).updateRedstone();
        }
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hit_x, float hit_y, float hit_z)
    {
        TileEntityFoundry te = (TileEntityFoundry) world.getTileEntity(pos);
        for (int i = 0; i < te.getTankCount(); i++)
        {
            if (FluidUtil.interactWithFluidHandler(player, hand, te.getTank(i)))
            {
                return true;
            }
        }

        int gui = -1;
        switch (state.getValue(MACHINE))
        {
        case CRUCIBLE_BASIC:
        case CRUCIBLE_STANDARD:
        case CRUCIBLE_ADVANCED:
            gui = CommonProxy.GUI_CRUCIBLE;
            break;
        case CASTER:
            gui = CommonProxy.GUI_CASTER;
            break;
        case ALLOY_MIXER:
            gui = CommonProxy.GUI_ALLOY_MIXER;
            break;
        case INFUSER:
            gui = CommonProxy.GUI_INFUSER;
            break;
        case MATERIAL_ROUTER:
            gui = CommonProxy.GUI_MATERIAL_ROUTER;
            break;
        case ALLOYING_CRUCIBLE:
            gui = CommonProxy.GUI_ALLOYING_CRUCIBLE;
            break;
        case FLUID_HEATER:
            gui = CommonProxy.GUI_FLUID_HEATER;
            break;
        default:
            return false;
        }
        if (!world.isRemote && gui >= 0)
        {
            player.openGui(Foundry.INSTANCE, gui, world, pos.getX(), pos.getY(), pos.getZ());
        }
        return true;
    }

}

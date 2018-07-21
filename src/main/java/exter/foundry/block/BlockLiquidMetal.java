package exter.foundry.block;

import java.util.List;
import java.util.Random;

import cofh.thermalfoundation.init.TFFluids;
import exter.foundry.creativetab.FoundryTab;
import exter.foundry.util.MiscUtil;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockLiquidMetal extends BlockFluidClassic
{
    private IBlockState solid_state;

    public BlockLiquidMetal(Fluid fluid, String name, IBlockState solid_state)
    {
        super(fluid, Material.LAVA);
        setLightOpacity(0);
        setLightLevel(1.0f);
        this.solid_state = solid_state;
        setUnlocalizedName(name);
        setCreativeTab(FoundryTab.INSTANCE);
        setRegistryName(name);
    }

    @Override
    public boolean canDisplace(IBlockAccess world, BlockPos pos)
    {
        if (world.getBlockState(pos).getMaterial().isLiquid())
        {
            return false;
        }
        return super.canDisplace(world, pos);
    }

    public void checkForHarden(World world, BlockPos pos, IBlockState state)
    {
        if (isSourceBlock(world, pos))
        {
            IBlockState solid = null;
            if (solid_state == null)
            {
                List<ItemStack> list = MiscUtil.getOresSafe("stoneVariant");
                if (!list.isEmpty())
                {
                    ItemStack item = list.get(Block.RANDOM.nextInt(list.size()));
                    if (item.getItem() instanceof ItemBlock)
                    {
                        solid = ((ItemBlock) item.getItem()).getBlock().getStateForPlacement(world, pos,
                                EnumFacing.NORTH, 0, 0, 0, item.getMetadata(), null, EnumHand.MAIN_HAND);
                    }
                }
            }
            else
            {
                solid = solid_state;
            }
            if (solid == null)
            {
                return;
            }
            for (EnumFacing facing : EnumFacing.VALUES)
            {
                tryToHarden(world, pos, pos.offset(facing), solid);
            }
        }
    }

    @Override
    public boolean displaceIfPossible(World world, BlockPos pos)
    {
        if (world.getBlockState(pos).getMaterial().isLiquid())
        {
            return false;
        }
        return super.displaceIfPossible(world, pos);
    }

    @Override
    public int getFireSpreadSpeed(IBlockAccess world, BlockPos pos, EnumFacing face)
    {
        return 300;
    }

    @Override
    public int getFlammability(IBlockAccess world, BlockPos pos, EnumFacing face)
    {
        return 0;
    }

    @Override
    public String getUnlocalizedName()
    {
        System.out.println(stack.getUnlocalizedName());
        return stack.getUnlocalizedName();
    }

    @Override
    public String getLocalizedName()
    {
        System.out.println(I18n.format(getUnlocalizedName()));
        return I18n.format(getUnlocalizedName());
    }

    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos fromPos)
    {
        super.neighborChanged(state, world, pos, block, fromPos);
        checkForHarden(world, pos, state);
    }

    @Override
    public void onBlockAdded(World world, BlockPos pos, IBlockState state)
    {
        super.onBlockAdded(world, pos, state);
        checkForHarden(world, pos, state);
    }

    @Override
    public void onEntityCollidedWithBlock(World wWorld, BlockPos pos, IBlockState state, Entity entity)
    {
        if (entity instanceof EntityLivingBase)
        {
            entity.motionX *= 0.5;
            entity.motionZ *= 0.5;
        }
        if (!entity.isImmuneToFire())
        {
            if (!(entity instanceof EntityItem))
            {
                entity.attackEntityFrom(DamageSource.LAVA, 4);
            }
            entity.setFire(15);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand)
    {
        if (temperature < 1200)
        {
            return;
        }
        double dx;
        double dy;
        double dz;

        if (world.getBlockState(pos.add(0, 1, 0)).getMaterial() == Material.AIR
                && !world.getBlockState(pos.add(0, 1, 0)).isOpaqueCube())
        {
            if (rand.nextInt(100) == 0)
            {
                dx = pos.getX() + rand.nextFloat();
                dy = pos.getY() + state.getBoundingBox(world, pos).maxY;
                dz = pos.getZ() + rand.nextFloat();
                world.spawnParticle(EnumParticleTypes.LAVA, dx, dy, dz, 0.0D, 0.0D, 0.0D);
                world.playSound(dx, dy, dz, SoundEvents.BLOCK_LAVA_POP, SoundCategory.BLOCKS,
                        0.2F + rand.nextFloat() * 0.2F, 0.9F + rand.nextFloat() * 0.15F, false);
            }

            if (rand.nextInt(200) == 0)
            {
                world.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_LAVA_AMBIENT,
                        SoundCategory.BLOCKS, 0.2F + rand.nextFloat() * 0.2F, 0.9F + rand.nextFloat() * 0.15F, false);
            }
        }

        BlockPos down = pos.down();
        if (rand.nextInt(10) == 0 && world.getBlockState(down).isSideSolid(world, down, EnumFacing.UP)
                && !world.getBlockState(pos.add(0, -1, 0)).getMaterial().blocksMovement())
        {
            dx = pos.getX() + rand.nextFloat();
            dy = pos.getY() - 1.05D;
            dz = pos.getZ() + rand.nextFloat();

            world.spawnParticle(EnumParticleTypes.DRIP_LAVA, dx, dy, dz, 0.0D, 0.0D, 0.0D);
        }
    }

    private boolean tryToHarden(World world, BlockPos pos, BlockPos npos, IBlockState state)
    {
        // Check if block is in contact with water.
        IBlockState nstate = world.getBlockState(npos);
        if (nstate.getBlock() == TFFluids.blockFluidCryotheum || nstate.getMaterial() == Material.WATER)
        {
            world.setBlockState(pos, state);
            world.playSound(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, SoundEvents.BLOCK_LAVA_EXTINGUISH,
                    SoundCategory.BLOCKS, 0.5f, 2.6f + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8f, false);
            for (int i = 0; i < 8; i++)
            {
                world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, pos.getX() + Math.random(), pos.getY() + 1.2D,
                        pos.getZ() + Math.random(), 0.0D, 0.0D, 0.0D);
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean shouldSideBeRendered(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side)
    {
        IBlockState neighbor = world.getBlockState(pos.offset(side));
        if (neighbor.getBlock() == this)
        {
            return false;
        }
        if (neighbor.getMaterial() == state.getMaterial())
        {
            return true;
        }
        return super.shouldSideBeRendered(state, world, pos, side);
    }
}

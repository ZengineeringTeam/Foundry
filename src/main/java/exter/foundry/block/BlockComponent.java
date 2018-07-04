package exter.foundry.block;

import exter.foundry.creativetab.FoundryTabMaterials;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockComponent extends Block implements IBlockVariants
{

    static public enum EnumVariant implements IStringSerializable
    {
        CASING_STANDARD("casing_standard", "componentBlockCasingStandard"),
        REFRACTORY_CLAY("block_refractoryclay", "componentBlockRefractoryClay"),
        CASING_ADVANCED("casing_advanced", "componentBlockCasingAdvanced"),
        CASING_BASIC("casing_basic", "componentBlockCasingBasic");

        public final String name;

        public final String model;

        private EnumVariant(String name, String model)
        {
            this.name = name;
            this.model = model;
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

    public static final PropertyEnum<EnumVariant> VARIANT = PropertyEnum.create("variant", EnumVariant.class);

    public BlockComponent()
    {
        super(Material.ROCK);
        setHardness(1.0F);
        setResistance(8.0F);
        setSoundType(SoundType.STONE);
        setUnlocalizedName("foundry.componentBlock");
        setCreativeTab(FoundryTabMaterials.INSTANCE);
        setRegistryName("componentBlock");
    }

    public ItemStack asItemStack(EnumVariant variant)
    {
        return new ItemStack(this, 1, variant.ordinal());
    }

    @Override
    public SoundType getSoundType(IBlockState state, World world, BlockPos pos, Entity entity)
    {
        if (state.getValue(VARIANT) == EnumVariant.REFRACTORY_CLAY)
            return SoundType.GROUND;
        return super.getSoundType(state, world, pos, entity);
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, VARIANT);
    }

    @Override
    public int damageDropped(IBlockState state)
    {
        return getMetaFromState(state);
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return state.getValue(VARIANT).ordinal();
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return getDefaultState().withProperty(VARIANT, EnumVariant.values()[meta]);
    }

    @Override
    public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> list)
    {
        for (EnumVariant m : EnumVariant.values())
        {
            list.add(new ItemStack(this, 1, m.ordinal()));
        }
    }

    @Override
    public String getUnlocalizedName(int meta)
    {
        return "tile.foundry." + getStateFromMeta(meta).getValue(VARIANT).model;
    }
}

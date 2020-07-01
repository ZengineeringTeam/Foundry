package exter.foundry.integration.crafttweaker;

import com.google.common.base.Preconditions;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import crafttweaker.api.block.IBlockState;
import exter.foundry.integration.ModIntegrationCrafttweaker;
import exter.foundry.tileentity.TileEntityHeatable;
import net.minecraft.block.Block;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.foundry.Heating")
public class CrTHeatingHandler
{
    public static class AddSourceAction implements IAction
    {
        private final IBlockState state;
        private final int heat;

        public AddSourceAction(IBlockState state, int heat)
        {
            this.state = state;
            this.heat = heat;
        }

        @Override
        public void apply()
        {
            Preconditions.checkNotNull(state);
            Block block = (Block) state.getBlock().getDefinition().getInternal();
            TileEntityHeatable.STATE_SOURCES.put(block.getStateFromMeta(state.getMeta()), heat);
        }

        @Override
        public String describe()
        {
            return null;
        }

    }

    @ZenMethod
    static public void addStateSource(IBlockState state, int heat)
    {
        ModIntegrationCrafttweaker.queueAdd(() -> {
            CraftTweakerAPI.apply(new AddSourceAction(state, heat));
        });
    }

}

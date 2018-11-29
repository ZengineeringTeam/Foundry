package exter.foundry.config;

import net.minecraftforge.common.config.Configuration;

import java.util.HashMap;
import java.util.Map;

public class MetalConfig {
    public enum IntegrationStrategy {
        ENABLED, DISABLED, NO_RECIPES
    }

    public static Map<String, IntegrationStrategy> metals = new HashMap<>();

    static {
        metals.put("iron", IntegrationStrategy.ENABLED);
        metals.put("gold", IntegrationStrategy.ENABLED);
        metals.put("copper", IntegrationStrategy.ENABLED);
        metals.put("tin", IntegrationStrategy.ENABLED);
        metals.put("bronze", IntegrationStrategy.ENABLED);
        metals.put("electrum", IntegrationStrategy.ENABLED);
        metals.put("invar", IntegrationStrategy.ENABLED);
        metals.put("nickel", IntegrationStrategy.ENABLED);
        metals.put("zinc", IntegrationStrategy.ENABLED);
        metals.put("brass", IntegrationStrategy.ENABLED);
        metals.put("silver", IntegrationStrategy.ENABLED);
        metals.put("steel", IntegrationStrategy.ENABLED);
        metals.put("constantan", IntegrationStrategy.ENABLED);
        metals.put("lead", IntegrationStrategy.ENABLED);
        metals.put("platinum", IntegrationStrategy.ENABLED);
        metals.put("aluminium", IntegrationStrategy.ENABLED);
        metals.put("alumina", IntegrationStrategy.ENABLED);
        metals.put("signalum", IntegrationStrategy.ENABLED);
        metals.put("lumium", IntegrationStrategy.ENABLED);
        metals.put("enderium", IntegrationStrategy.ENABLED);
        metals.put("uranium", IntegrationStrategy.ENABLED);
        metals.put("cobalt", IntegrationStrategy.ENABLED);
        metals.put("iridium", IntegrationStrategy.ENABLED);
        metals.put("glass", IntegrationStrategy.ENABLED);
    }

    public static void load(Configuration config) {
        for (Map.Entry<String, IntegrationStrategy> strategy : metals.entrySet()) {
            String newStrategy = config.getString(strategy.getKey(), "Base Metals", strategy.getValue().name(), "Valid values: ENABLED, DISABLED, NO_RECIPES");
            metals.put(strategy.getKey(), IntegrationStrategy.valueOf(newStrategy));
        }
    }
}

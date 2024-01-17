package me.infamous.kanekaido;

import me.infamous.kanekaido.client.keybindings.KKKeyBinding;
import me.infamous.kanekaido.client.rendering.DragonKaidoRenderer;
import me.infamous.kanekaido.client.rendering.EnergyBeamRenderer;
import me.infamous.kanekaido.client.rendering.KaidoRenderer;
import me.infamous.kanekaido.common.network.NetworkHandler;
import me.infamous.kanekaido.common.registry.KKEntityTypes;
import me.infamous.kanekaido.common.registry.KKItems;
import net.minecraft.world.GameRules;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Method;

@Mod(KaneKaido.MODID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class KaneKaido {
    public static final String MODID = "kanekaido";
    public static final Logger LOGGER = LogManager.getLogger();
    private static final GameRules.RuleKey<GameRules.BooleanValue> RULE_KAIDO_ABILITIES = GameRules.register("kaidoAbilities", GameRules.Category.PLAYER, createBooleanGameRule(true));
    private static final GameRules.RuleKey<GameRules.BooleanValue> RULE_KAIDO_MORPHS = GameRules.register("kaidoMorphs", GameRules.Category.PLAYER, createBooleanGameRule(true));

    public KaneKaido() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        KKEntityTypes.register(modEventBus);
        KKItems.register(modEventBus);
    }

    @SubscribeEvent
    public static void onCommonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(NetworkHandler::init);
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        KKKeyBinding.registerKeyBindings();

        RenderingRegistry.registerEntityRenderingHandler(KKEntityTypes.ENERGY_BEAM.get(), EnergyBeamRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(KKEntityTypes.KAIDO.get(), KaidoRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(KKEntityTypes.DRAGON_KAIDO.get(), DragonKaidoRenderer::new);


    }

    public static GameRules.RuleKey<GameRules.BooleanValue> getRuleKaidoAbilities() {
        return RULE_KAIDO_ABILITIES;
    }

    public static GameRules.RuleKey<GameRules.BooleanValue> getRuleKaidoMorphs() {
        return RULE_KAIDO_MORPHS;
    }

    private static GameRules.RuleType<GameRules.BooleanValue> createBooleanGameRule(boolean defaultValue) {
        Method create = ObfuscationReflectionHelper.findMethod(GameRules.BooleanValue.class, "func_223568_b", boolean.class);
        try {
            return (GameRules.RuleType<GameRules.BooleanValue>)create.invoke(null, defaultValue);
        } catch (Exception e) {
            LOGGER.info("Failed to create game rule!");
            return null;
        }
    }
}

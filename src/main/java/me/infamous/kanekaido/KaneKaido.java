package me.infamous.kanekaido;

import me.infamous.kanekaido.client.keybindings.KKKeyBinding;
import me.infamous.kanekaido.common.network.NetworkHandler;
import me.infamous.kanekaido.common.registry.KKEntityTypes;
import me.infamous.kanekaido.common.registry.KKItems;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(KaneKaido.MODID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class KaneKaido {
    public static final String MODID = "kanekaido";
    public static final Logger LOGGER = LogManager.getLogger();

    public KaneKaido() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        KKEntityTypes.register(modEventBus);
        KKItems.register(modEventBus);
    }

    @SubscribeEvent
    public static void onCommonSetup(FMLCommonSetupEvent event) {
        NetworkHandler.init();
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        ClientRegistry.registerKeyBinding(KKKeyBinding.keyFireball);
        ClientRegistry.registerKeyBinding(KKKeyBinding.keyAirSlash);
        ClientRegistry.registerKeyBinding(KKKeyBinding.keyEnergyBeam);
    }
}

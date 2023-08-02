package me.infamous.kanekaido.common.events;

import me.infamous.kanekaido.KaneKaido;
import me.infamous.kanekaido.common.entities.DragonKaido;
import me.infamous.kanekaido.common.entities.Kaido;
import me.infamous.kanekaido.common.registry.KKEntityTypes;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = KaneKaido.MODID)
public class ModEventHandler {

    @SubscribeEvent
    static void registerAttributes(EntityAttributeCreationEvent event){
        event.put(KKEntityTypes.KAIDO.get(), Kaido.createAttributes().build());
        event.put(KKEntityTypes.DRAGON_KAIDO.get(), DragonKaido.createAttributes().build());
    }
}

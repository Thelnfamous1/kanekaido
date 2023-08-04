package me.infamous.kanekaido.client.events;

import me.infamous.kanekaido.KaneKaido;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = KaneKaido.MODID, value = Dist.CLIENT)
public class ModClientEventHandler {
}

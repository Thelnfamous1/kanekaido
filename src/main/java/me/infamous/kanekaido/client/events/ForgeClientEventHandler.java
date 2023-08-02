package me.infamous.kanekaido.client.events;

import me.infamous.kanekaido.KaneKaido;
import me.infamous.kanekaido.client.keybindings.KKKeyBinding;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, modid = KaneKaido.MODID, value = Dist.CLIENT)
public class ForgeClientEventHandler {

    @SubscribeEvent
    public static void onKeyInput(InputEvent.KeyInputEvent event){
        Minecraft minecraft = Minecraft.getInstance();
        ClientPlayerEntity clientPlayer = minecraft.player;
        if(clientPlayer != null){
            int key = event.getKey();
            KKKeyBinding.handleAllKeys(key, clientPlayer);
        }
    }
}

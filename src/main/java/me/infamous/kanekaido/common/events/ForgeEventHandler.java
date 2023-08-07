package me.infamous.kanekaido.common.events;

import me.ichun.mods.morph.common.morph.MorphHandler;
import me.infamous.kanekaido.KaneKaido;
import me.infamous.kanekaido.common.command.KaidoToggleCommand;
import me.infamous.kanekaido.common.entities.Kaido;
import me.infamous.kanekaido.common.registry.KKEntityTypes;
import net.minecraft.entity.LivingEntity;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, modid = KaneKaido.MODID)
public class ForgeEventHandler {

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event)
    {
        if(event.side == LogicalSide.CLIENT
                && event.phase == TickEvent.Phase.END
                && !event.player.removed
                && event.player.getId() > 0)
        {
            LivingEntity activeMorphEntity = MorphHandler.INSTANCE.getActiveMorphEntity(event.player);
            if(activeMorphEntity != null && activeMorphEntity.getType() == KKEntityTypes.KAIDO.get()){
                ((Kaido)activeMorphEntity).tickDownAttackTimers();
            }
        }
    }

    @SubscribeEvent
    static void onRegisterCommands(RegisterCommandsEvent event){
        //KaidoToggleCommand.register(event.getDispatcher());
    }

}

package me.infamous.kanekaido.client.network;

import me.ichun.mods.morph.common.morph.MorphHandler;
import me.infamous.kanekaido.common.abilities.KaidoAttack;
import me.infamous.kanekaido.common.entities.Kaido;
import me.infamous.kanekaido.common.network.ClientboundSpecialAttackPacket;
import me.infamous.kanekaido.common.registry.KKEntityTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;

public class ClientNetworkHandler {
    public static void handleSpecialAttackPacket(ClientboundSpecialAttackPacket packet) {
        Entity entity = Minecraft.getInstance().level.getEntity(packet.getId());
        if(entity instanceof PlayerEntity){
            KaidoAttack attackType = packet.getAttack();
            LivingEntity activeMorphEntity = MorphHandler.INSTANCE.getActiveMorphEntity((PlayerEntity) entity);
            if(activeMorphEntity != null && activeMorphEntity.getType() == KKEntityTypes.KAIDO.get()){
                attackType.getKaidoAttack().accept(((Kaido)activeMorphEntity));
            }
        }
    }
}

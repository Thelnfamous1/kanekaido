package me.infamous.kanekaido.common.network;

import me.ichun.mods.morph.common.morph.MorphHandler;
import me.infamous.kanekaido.common.abilities.KaidoAttack;
import me.infamous.kanekaido.common.entities.Kaido;
import me.infamous.kanekaido.common.registry.KKEntityTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class ClientboundSpecialAttackPacket {
    private final KaidoAttack attack;

    public ClientboundSpecialAttackPacket(KaidoAttack attack){
        this.attack = attack;
    }

    public static ClientboundSpecialAttackPacket decode(PacketBuffer packetBuffer){
        KaidoAttack morph = packetBuffer.readEnum(KaidoAttack.class);
        return new ClientboundSpecialAttackPacket(morph);
    }

    public static void encode(ClientboundSpecialAttackPacket packet, PacketBuffer packetBuffer){
        packetBuffer.writeEnum(packet.attack);
    }

    public static void handle(ClientboundSpecialAttackPacket packet, Supplier<NetworkEvent.Context> ctx){
        ctx.get().enqueueWork(()->{
            ClientPlayerEntity clientPlayer = Minecraft.getInstance().player;
            if(clientPlayer == null) return;

            KaidoAttack attackType = packet.getAttack();

            LivingEntity activeMorphEntity = MorphHandler.INSTANCE.getActiveMorphEntity(clientPlayer);
            if(activeMorphEntity != null && activeMorphEntity.getType() == KKEntityTypes.KAIDO.get()){
                attackType.getKaidoAttack().accept(((Kaido)activeMorphEntity));
            }
        });
        ctx.get().setPacketHandled(true);
    }

    public KaidoAttack getAttack() {
        return this.attack;
    }

}

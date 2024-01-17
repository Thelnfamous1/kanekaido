package me.infamous.kanekaido.common.network;

import me.ichun.mods.morph.common.morph.MorphHandler;
import me.infamous.kanekaido.common.abilities.KaidoAttack;
import me.infamous.kanekaido.common.entities.Kaido;
import me.infamous.kanekaido.common.registry.KKEntityTypes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.function.Supplier;

public class ServerboundSpecialAttackPacket {
    private final KaidoAttack attack;

    public ServerboundSpecialAttackPacket(KaidoAttack attack){
        this.attack = attack;
    }

    public static ServerboundSpecialAttackPacket decode(PacketBuffer packetBuffer){
        KaidoAttack morph = packetBuffer.readEnum(KaidoAttack.class);
        return new ServerboundSpecialAttackPacket(morph);
    }

    public static void encode(ServerboundSpecialAttackPacket packet, PacketBuffer packetBuffer){
        packetBuffer.writeEnum(packet.attack);
    }

    public static void handle(ServerboundSpecialAttackPacket packet, Supplier<NetworkEvent.Context> ctx){
        ctx.get().enqueueWork(()->{
            ServerPlayerEntity serverPlayer = ctx.get().getSender();
            if(serverPlayer == null) return;

            KaidoAttack attackType = packet.getAttack();

            LivingEntity activeMorphEntity = MorphHandler.INSTANCE.getActiveMorphEntity(serverPlayer);
            if(activeMorphEntity != null && activeMorphEntity.getType() == KKEntityTypes.KAIDO.get()){
                attackType.getKaidoAttack().accept(((Kaido)activeMorphEntity));
                NetworkHandler.INSTANCE.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> serverPlayer), new ClientboundSpecialAttackPacket(serverPlayer, packet.attack));
            }
        });
        ctx.get().setPacketHandled(true);
    }

    public KaidoAttack getAttack() {
        return this.attack;
    }

}

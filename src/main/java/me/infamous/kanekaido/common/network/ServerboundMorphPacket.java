package me.infamous.kanekaido.common.network;

import me.ichun.mods.morph.common.morph.MorphHandler;
import me.infamous.kanekaido.KaneKaido;
import me.infamous.kanekaido.common.morph.KaidoMorph;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class ServerboundMorphPacket {
    private final KaidoMorph morph;

    public ServerboundMorphPacket(KaidoMorph morph){
        this.morph = morph;
    }

    public static ServerboundMorphPacket decode(PacketBuffer packetBuffer){
        KaidoMorph morph = packetBuffer.readEnum(KaidoMorph.class);
        return new ServerboundMorphPacket(morph);
    }

    public static void encode(ServerboundMorphPacket packet, PacketBuffer packetBuffer){
        packetBuffer.writeEnum(packet.morph);
    }

    public static void handle(ServerboundMorphPacket packet, Supplier<NetworkEvent.Context> ctx){
        ctx.get().enqueueWork(()->{
            ServerPlayerEntity serverPlayer = ctx.get().getSender();
            if(serverPlayer == null) return;
            if(!serverPlayer.level.getGameRules().getBoolean(KaneKaido.getRuleKaidoMorphs())) return;
            World world = serverPlayer.level;

            KaidoMorph morph = packet.getMorph();

            if(MorphHandler.INSTANCE.getMorphInfo(serverPlayer).isCurrentlyThisVariant(morph.getMorphVariant(world).thisVariant)){
                MorphHandler.INSTANCE.demorph(serverPlayer);
            } else{
                MorphHandler.INSTANCE.morphTo(serverPlayer, morph.getMorphVariant(world));
            }
        });
        ctx.get().setPacketHandled(true);
    }

    public KaidoMorph getMorph() {
        return this.morph;
    }

}

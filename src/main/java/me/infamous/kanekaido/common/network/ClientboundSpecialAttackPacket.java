package me.infamous.kanekaido.common.network;

import me.infamous.kanekaido.client.network.ClientNetworkHandler;
import me.infamous.kanekaido.common.abilities.KaidoAttack;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class ClientboundSpecialAttackPacket {
    private final int id;
    private final KaidoAttack attack;

    public ClientboundSpecialAttackPacket(Entity entity, KaidoAttack attack){
        this(entity.getId(), attack);
    }

    private ClientboundSpecialAttackPacket(int entityId, KaidoAttack attack){
        this.id = entityId;
        this.attack = attack;
    }

    public static ClientboundSpecialAttackPacket decode(PacketBuffer packetBuffer){
        int id = packetBuffer.readInt();
        KaidoAttack morph = packetBuffer.readEnum(KaidoAttack.class);
        return new ClientboundSpecialAttackPacket(id, morph);
    }

    public static void encode(ClientboundSpecialAttackPacket packet, PacketBuffer packetBuffer){
        packetBuffer.writeInt(packet.id);
        packetBuffer.writeEnum(packet.attack);
    }

    public static void handle(ClientboundSpecialAttackPacket packet, Supplier<NetworkEvent.Context> ctx){
        ctx.get().enqueueWork(()-> ClientNetworkHandler.handleSpecialAttackPacket(packet));
        ctx.get().setPacketHandled(true);
    }

    public int getId(){
        return this.id;
    }

    public KaidoAttack getAttack() {
        return this.attack;
    }

}

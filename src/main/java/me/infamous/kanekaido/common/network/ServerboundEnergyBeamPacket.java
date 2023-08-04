package me.infamous.kanekaido.common.network;

import me.infamous.kanekaido.common.entities.EnergyBeam;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class ServerboundEnergyBeamPacket {

    private final int energyBeamId;
    private final double positionX;
    private final double positionY;
    private final double positionZ;
    private final float xRot;
    private final float yRot;
    private final float xRotO;
    private final float yRotO;

    public ServerboundEnergyBeamPacket(EnergyBeam energyBeam) {
        this.energyBeamId = energyBeam.getId();
        this.positionX = energyBeam.position().x;
        this.positionY = energyBeam.position().y;
        this.positionZ = energyBeam.position().z;
        this.xRot = energyBeam.xRot;
        this.yRot = energyBeam.yRot;
        this.xRotO = energyBeam.xRotO;
        this.yRotO = energyBeam.yRotO;
    }

    public ServerboundEnergyBeamPacket(int energyBeamId, double positionX, double positionY, double positionZ, float xRot, float yRot, float xRotO, float yRotO) {
        this.energyBeamId = energyBeamId;
        this.positionX = positionX;
        this.positionY = positionY;
        this.positionZ = positionZ;
        this.xRot = xRot;
        this.yRot = yRot;
        this.xRotO = xRotO;
        this.yRotO = yRotO;
    }

    public static void encode(ServerboundEnergyBeamPacket packet, PacketBuffer buf) {
        buf.writeInt(packet.energyBeamId);
        buf.writeDouble(packet.positionX);
        buf.writeDouble(packet.positionY);
        buf.writeDouble(packet.positionZ);
        buf.writeFloat(packet.xRot);
        buf.writeFloat(packet.yRot);
        buf.writeFloat(packet.xRotO);
        buf.writeFloat(packet.yRotO);
    }

    public static ServerboundEnergyBeamPacket decode(PacketBuffer buf) {
        return new ServerboundEnergyBeamPacket(
                buf.readInt(),
                buf.readDouble(),
                buf.readDouble(),
                buf.readDouble(),
                buf.readFloat(),
                buf.readFloat(),
                buf.readFloat(),
                buf.readFloat()
        );
    }

    public static void handle(ServerboundEnergyBeamPacket packet, Supplier<NetworkEvent.Context> ctx) {
        if (packet != null) {
            ctx.get().setPacketHandled(true);
            ctx.get().enqueueWork(() -> {
                ServerPlayerEntity player = ctx.get().getSender();
                if (player != null) {
                    Entity entity = player.level.getEntity(packet.energyBeamId);
                    if(entity instanceof EnergyBeam) {
                        EnergyBeam energyBeam = (EnergyBeam) entity;
                        if(energyBeam.getOwner() != player) return;
                        energyBeam.setPos(packet.positionX, packet.positionY, packet.positionZ);
                        energyBeam.xRot = packet.xRot;
                        energyBeam.yRot = packet.yRot;
                        energyBeam.xRotO = packet.xRotO;
                        energyBeam.yRotO = packet.yRotO;
                    }
                }
            });
        }
    }
}
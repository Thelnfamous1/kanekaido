package me.infamous.kanekaido.common.network;

import me.infamous.kanekaido.KaneKaido;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

import java.util.Optional;

public final class NetworkHandler {
    public static final SimpleChannel INSTANCE = NetworkRegistry.ChannelBuilder.named(
            new ResourceLocation(KaneKaido.MODID, "network"))
            .clientAcceptedVersions("1"::equals)
            .serverAcceptedVersions("1"::equals)
            .networkProtocolVersion(() -> "1")
            .simpleChannel();

    private static int PACKET_COUNTER = 0;

    public NetworkHandler() {
    }

    public static void init() {
        INSTANCE.registerMessage(
                incrementAndGetPacketCounter(),
                ServerboundAbilityPacket.class,
                ServerboundAbilityPacket::encode,
                ServerboundAbilityPacket::decode,
                ServerboundAbilityPacket::handle,
                Optional.of(NetworkDirection.PLAY_TO_SERVER));
        INSTANCE.registerMessage(
                incrementAndGetPacketCounter(),
                ServerboundEnergyBeamPacket.class,
                ServerboundEnergyBeamPacket::encode,
                ServerboundEnergyBeamPacket::decode,
                ServerboundEnergyBeamPacket::handle,
                Optional.of(NetworkDirection.PLAY_TO_SERVER));
        INSTANCE.registerMessage(
                incrementAndGetPacketCounter(),
                ServerboundMorphPacket.class,
                ServerboundMorphPacket::encode,
                ServerboundMorphPacket::decode,
                ServerboundMorphPacket::handle,
                Optional.of(NetworkDirection.PLAY_TO_SERVER));
        INSTANCE.registerMessage(
                incrementAndGetPacketCounter(),
                ServerboundSpecialAttackPacket.class,
                ServerboundSpecialAttackPacket::encode,
                ServerboundSpecialAttackPacket::decode,
                ServerboundSpecialAttackPacket::handle,
                Optional.of(NetworkDirection.PLAY_TO_SERVER));
        INSTANCE.registerMessage(
                incrementAndGetPacketCounter(),
                ClientboundSpecialAttackPacket.class,
                ClientboundSpecialAttackPacket::encode,
                ClientboundSpecialAttackPacket::decode,
                ClientboundSpecialAttackPacket::handle,
                Optional.of(NetworkDirection.PLAY_TO_CLIENT));
    }

    public static int incrementAndGetPacketCounter() {
        return PACKET_COUNTER++;
    }

}
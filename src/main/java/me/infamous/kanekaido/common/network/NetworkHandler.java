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
                ServerboundAbilityPacket::encodePacket,
                ServerboundAbilityPacket::decodePacket,
                ServerboundAbilityPacket::handlePacket,
                Optional.of(NetworkDirection.PLAY_TO_SERVER));
    }

    public static int incrementAndGetPacketCounter() {
        return PACKET_COUNTER++;
    }

}
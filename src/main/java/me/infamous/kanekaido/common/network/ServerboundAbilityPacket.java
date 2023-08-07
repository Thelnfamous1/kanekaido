package me.infamous.kanekaido.common.network;

import me.infamous.kanekaido.KaneKaido;
import me.infamous.kanekaido.common.abilities.KaidoAbility;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class ServerboundAbilityPacket {
    private final KeyBindAction keyBindAction;
    private final KaidoAbility ability;

    public ServerboundAbilityPacket(KeyBindAction keyBindAction, KaidoAbility ability){
        this.keyBindAction = keyBindAction;
        this.ability = ability;
    }

    public static ServerboundAbilityPacket decode(PacketBuffer packetBuffer){
        KeyBindAction keyBindAction = packetBuffer.readEnum(KeyBindAction.class);
        KaidoAbility ability = packetBuffer.readEnum(KaidoAbility.class);
        return new ServerboundAbilityPacket(keyBindAction, ability);
    }

    public static void encode(ServerboundAbilityPacket packet, PacketBuffer packetBuffer){
        packetBuffer.writeEnum(packet.keyBindAction);
        packetBuffer.writeEnum(packet.ability);
    }

    public static void handle(ServerboundAbilityPacket packet, Supplier<NetworkEvent.Context> ctx){
        ctx.get().enqueueWork(()->{
            ServerPlayerEntity serverPlayer = ctx.get().getSender();
            if(serverPlayer == null) return;
            if(!serverPlayer.level.getGameRules().getBoolean(KaneKaido.getRuleKaidoAbilities())) return;


            KaidoAbility ability = packet.getAbility();
            ability.getHandlerForKeyBindAction(packet.getKeyBindAction()).accept(serverPlayer);

            /*
            KKAbilityCap abilityCap = CapabilityHelper.getFalconAbilityCap(serverPlayer);
            if(abilityCap != null){
                KaidoAbility ability = packet.getAbility();
                ability.getHandlerForKeyBindAction(packet.getKeyBindAction()).accept(serverPlayer);
            }
             */
        });
        ctx.get().setPacketHandled(true);
    }

    public KeyBindAction getKeyBindAction() {
        return this.keyBindAction;
    }

    public KaidoAbility getAbility() {
        return this.ability;
    }

}

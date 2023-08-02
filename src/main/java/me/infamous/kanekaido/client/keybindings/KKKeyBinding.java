package me.infamous.kanekaido.client.keybindings;

import me.infamous.kanekaido.common.abilities.KaidoAbility;
import me.infamous.kanekaido.common.network.KeyBindAction;
import me.infamous.kanekaido.common.network.NetworkHandler;
import me.infamous.kanekaido.common.network.ServerboundAbilityPacket;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraftforge.client.settings.IKeyConflictContext;
import net.minecraftforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;

import java.util.function.Consumer;

public class KKKeyBinding extends KeyBinding{

    private boolean held;
    private boolean initialPress;
    private boolean initialRelease;

    private final Consumer<ClientPlayerEntity> onInitialPress;
    private final Consumer<ClientPlayerEntity> onHeld;
    private final Consumer<ClientPlayerEntity> onInitialRelease;

    public KKKeyBinding(String description,
                        IKeyConflictContext keyConflictContext,
                        final InputMappings.Type inputType,
                        final int keyCode,
                        String category,
                        Consumer<ClientPlayerEntity> onInitialPress,
                        Consumer<ClientPlayerEntity> onHeld,
                        Consumer<ClientPlayerEntity> onInitialRelease){
        super(description, keyConflictContext, inputType, keyCode, category);
        this.onInitialPress = onInitialPress;
        this.onHeld = onHeld;
        this.onInitialRelease = onInitialRelease;
    }

    public void handleKey(ClientPlayerEntity clientPlayer){
        if(this.initialPress){
            this.onInitialPress.accept(clientPlayer);
        } else if(this.held){
            this.onHeld.accept(clientPlayer);
        } else if(this.initialRelease){
            this.onInitialRelease.accept(clientPlayer);
        }
    }

    @Override
    public void setDown(boolean down) {
        boolean wasDown = this.isDown();
        super.setDown(down);
        this.initialPress = !wasDown && this.isDown();
        this.held = wasDown && this.isDown();
        this.initialRelease = wasDown && !this.isDown();
    }

    public static final int FIREBALL_KEYCODE = GLFW.GLFW_KEY_R;
    public static final int AIR_SLASH_KEYCODE = GLFW.GLFW_KEY_O;
    public static final int ENERGY_BEAM_KEYCODE = GLFW.GLFW_KEY_H;

    public static final String ABILITY_KEY_CATEGORY = "key.kanekaido.categories.abilities";
    public static final String MORPH_KEY_CATEGORY = "key.kanekaido.categories.morph";


    public static final String FIREBALL_DESCRIPTION_KEY = "key.kanekaido.fireball";
    public static final KKKeyBinding keyFireball =
            new KKKeyBinding(
                    FIREBALL_DESCRIPTION_KEY,
                    KeyConflictContext.IN_GAME,
                    InputMappings.Type.KEYSYM,
                    FIREBALL_KEYCODE,
                    ABILITY_KEY_CATEGORY,
                    (clientPlayer) -> NetworkHandler.INSTANCE.sendToServer(new ServerboundAbilityPacket(KeyBindAction.INITIAL_PRESS, KaidoAbility.FIREBALL)),
                    (clientPlayer) -> NetworkHandler.INSTANCE.sendToServer(new ServerboundAbilityPacket(KeyBindAction.HELD, KaidoAbility.FIREBALL)),
                    (clientPlayer) -> NetworkHandler.INSTANCE.sendToServer(new ServerboundAbilityPacket(KeyBindAction.RELEASE, KaidoAbility.FIREBALL)));


    public static final String AIR_SLASH_DESCRIPTION_KEY = "key.kanekaido.air_slash";
    public static final KKKeyBinding keyAirSlash =
            new KKKeyBinding(
                    AIR_SLASH_DESCRIPTION_KEY,
                    KeyConflictContext.IN_GAME,
                    InputMappings.Type.KEYSYM,
                    AIR_SLASH_KEYCODE,
                    ABILITY_KEY_CATEGORY,
                    (clientPlayer) -> NetworkHandler.INSTANCE.sendToServer(new ServerboundAbilityPacket(KeyBindAction.INITIAL_PRESS, KaidoAbility.AIR_SLASH)),
                    (clientPlayer) -> NetworkHandler.INSTANCE.sendToServer(new ServerboundAbilityPacket(KeyBindAction.HELD, KaidoAbility.AIR_SLASH)),
                    (clientPlayer) -> NetworkHandler.INSTANCE.sendToServer(new ServerboundAbilityPacket(KeyBindAction.RELEASE, KaidoAbility.AIR_SLASH)));


    public static final String FIRE_BEAM_DESCRIPTION_KEY = "key.kanekaido.fire_beam";
    public static final KKKeyBinding keyEnergyBeam =
            new KKKeyBinding(
                    FIRE_BEAM_DESCRIPTION_KEY,
                    KeyConflictContext.IN_GAME,
                    InputMappings.Type.KEYSYM,
                    ENERGY_BEAM_KEYCODE,
                    ABILITY_KEY_CATEGORY,
                    (clientPlayer) -> NetworkHandler.INSTANCE.sendToServer(new ServerboundAbilityPacket(KeyBindAction.INITIAL_PRESS, KaidoAbility.ENERGY_BEAM)),
                    (clientPlayer) -> NetworkHandler.INSTANCE.sendToServer(new ServerboundAbilityPacket(KeyBindAction.HELD, KaidoAbility.ENERGY_BEAM)),
                    (clientPlayer) -> NetworkHandler.INSTANCE.sendToServer(new ServerboundAbilityPacket(KeyBindAction.RELEASE, KaidoAbility.ENERGY_BEAM)));


    public static void handleAllKeys(int key, ClientPlayerEntity clientPlayer) {
        if(key == keyFireball.getKey().getValue()){
            keyFireball.handleKey(clientPlayer);
        }
        if(key == keyAirSlash.getKey().getValue()){
            keyAirSlash.handleKey(clientPlayer);
        }
        if(key == keyEnergyBeam.getKey().getValue()){
            keyEnergyBeam.handleKey(clientPlayer);
        }
    }
}
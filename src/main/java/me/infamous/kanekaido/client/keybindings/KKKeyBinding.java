package me.infamous.kanekaido.client.keybindings;

import me.infamous.kanekaido.KaneKaido;
import me.infamous.kanekaido.common.abilities.KaidoAbility;
import me.infamous.kanekaido.common.abilities.KaidoAttack;
import me.infamous.kanekaido.common.morph.KaidoMorph;
import me.infamous.kanekaido.common.network.*;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraftforge.client.settings.IKeyConflictContext;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.fml.client.registry.ClientRegistry;
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

    public static void registerKeyBindings() {
        ClientRegistry.registerKeyBinding(keyFireball);
        ClientRegistry.registerKeyBinding(keyAirSlash);
        ClientRegistry.registerKeyBinding(keyEnergyBeam);
        ClientRegistry.registerKeyBinding(keyKaidoMorph);
        ClientRegistry.registerKeyBinding(keyDragonKaidoMorph);
        ClientRegistry.registerKeyBinding(keyKaidoAttackA);
        ClientRegistry.registerKeyBinding(keyKaidoAttackB);
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

    public static final int FIREBALL_KEYCODE = GLFW.GLFW_KEY_I;
    public static final int AIR_SLASH_KEYCODE = GLFW.GLFW_KEY_O;
    public static final int ENERGY_BEAM_KEYCODE = GLFW.GLFW_KEY_P;
    public static final int KAIDO_MORPH_KEYCODE = GLFW.GLFW_KEY_K;
    public static final int DRAGON_KAIDO_MORPH_KEYCODE = GLFW.GLFW_KEY_L;
    public static final int KAIDO_ATTACK_A_KEYCODE = GLFW.GLFW_KEY_N;
    public static final int KAIDO_ATTACK_B_KEYCODE = GLFW.GLFW_KEY_M;

    public static final String ABILITY_KEY_CATEGORY = "key.kanekaido.categories.abilities";
    public static final String ATTACK_KEY_CATEGORY = "key.kanekaido.categories.attack";
    public static final String MORPH_KEY_CATEGORY = "key.kanekaido.categories.morph";

    public static final String FIREBALL_DESCRIPTION_KEY = "key.kanekaido.fireball";
    public static final KKKeyBinding keyFireball =
            new KKKeyBinding(
                    FIREBALL_DESCRIPTION_KEY,
                    KeyConflictContext.IN_GAME,
                    InputMappings.Type.KEYSYM,
                    FIREBALL_KEYCODE,
                    ABILITY_KEY_CATEGORY,
                    (clientPlayer) -> sendAbilityPacket(clientPlayer, KeyBindAction.INITIAL_PRESS, KaidoAbility.FIREBALL),
                    (clientPlayer) -> sendAbilityPacket(clientPlayer, KeyBindAction.HELD, KaidoAbility.FIREBALL),
                    (clientPlayer) -> sendAbilityPacket(clientPlayer, KeyBindAction.RELEASE, KaidoAbility.FIREBALL));


    public static final String AIR_SLASH_DESCRIPTION_KEY = "key.kanekaido.air_slash";
    public static final KKKeyBinding keyAirSlash =
            new KKKeyBinding(
                    AIR_SLASH_DESCRIPTION_KEY,
                    KeyConflictContext.IN_GAME,
                    InputMappings.Type.KEYSYM,
                    AIR_SLASH_KEYCODE,
                    ABILITY_KEY_CATEGORY,
                    (clientPlayer) -> sendAbilityPacket(clientPlayer, KeyBindAction.INITIAL_PRESS, KaidoAbility.AIR_SLASH),
                    (clientPlayer) -> sendAbilityPacket(clientPlayer, KeyBindAction.HELD, KaidoAbility.AIR_SLASH),
                    (clientPlayer) -> sendAbilityPacket(clientPlayer, KeyBindAction.RELEASE, KaidoAbility.AIR_SLASH));

    private static void sendAbilityPacket(ClientPlayerEntity clientPlayer, KeyBindAction initialPress, KaidoAbility airSlash) {
        if(!clientPlayer.level.getGameRules().getBoolean(KaneKaido.getRuleKaidoAbilities())) return;
        NetworkHandler.INSTANCE.sendToServer(new ServerboundAbilityPacket(initialPress, airSlash));
    }


    public static final String FIRE_BEAM_DESCRIPTION_KEY = "key.kanekaido.fire_beam";
    public static final KKKeyBinding keyEnergyBeam =
            new KKKeyBinding(
                    FIRE_BEAM_DESCRIPTION_KEY,
                    KeyConflictContext.IN_GAME,
                    InputMappings.Type.KEYSYM,
                    ENERGY_BEAM_KEYCODE,
                    ABILITY_KEY_CATEGORY,
                    (clientPlayer) -> sendAbilityPacket(clientPlayer, KeyBindAction.INITIAL_PRESS, KaidoAbility.ENERGY_BEAM),
                    (clientPlayer) -> sendAbilityPacket(clientPlayer, KeyBindAction.HELD, KaidoAbility.ENERGY_BEAM),
                    (clientPlayer) -> sendAbilityPacket(clientPlayer, KeyBindAction.RELEASE, KaidoAbility.ENERGY_BEAM));


    public static final String KAIDO_MORPH_DESCRIPTION_KEY = "key.kanekaido.kaido_morph";
    public static final KKKeyBinding keyKaidoMorph =
            new KKKeyBinding(
                    KAIDO_MORPH_DESCRIPTION_KEY,
                    KeyConflictContext.IN_GAME,
                    InputMappings.Type.KEYSYM,
                    KAIDO_MORPH_KEYCODE,
                    MORPH_KEY_CATEGORY,
                    (clientPlayer) -> sendMorphPacket(clientPlayer, KaidoMorph.KAIDO),
                    (clientPlayer) -> {},
                    (clientPlayer) -> {});

    private static void sendMorphPacket(ClientPlayerEntity clientPlayer, KaidoMorph kaido) {
        if(!clientPlayer.level.getGameRules().getBoolean(KaneKaido.getRuleKaidoMorphs())) return;
        NetworkHandler.INSTANCE.sendToServer(new ServerboundMorphPacket(kaido));
    }

    public static final String DRAGON_KAIDO_MORPH_DESCRIPTION_KEY = "key.kanekaido.dragon_kaido_morph";
    public static final KKKeyBinding keyDragonKaidoMorph =
            new KKKeyBinding(
                    DRAGON_KAIDO_MORPH_DESCRIPTION_KEY,
                    KeyConflictContext.IN_GAME,
                    InputMappings.Type.KEYSYM,
                    DRAGON_KAIDO_MORPH_KEYCODE,
                    MORPH_KEY_CATEGORY,
                    (clientPlayer) -> sendMorphPacket(clientPlayer, KaidoMorph.DRAGON_KAIDO),
                    (clientPlayer) -> {},
                    (clientPlayer) -> {});

    public static final String KAIDO_ATTACK_A_DESCRIPTION_KEY = "key.kanekaido.kaido_attack_a";
    public static final KKKeyBinding keyKaidoAttackA =
            new KKKeyBinding(
                    KAIDO_ATTACK_A_DESCRIPTION_KEY,
                    KeyConflictContext.IN_GAME,
                    InputMappings.Type.KEYSYM,
                    KAIDO_ATTACK_A_KEYCODE,
                    ATTACK_KEY_CATEGORY,
                    (clientPlayer) -> NetworkHandler.INSTANCE.sendToServer(new ServerboundSpecialAttackPacket(KaidoAttack.ATTACK_A)),
                    (clientPlayer) -> {},
                    (clientPlayer) -> {});

    public static final String KAIDO_ATTACK_B_DESCRIPTION_KEY = "key.kanekaido.kaido_attack_b";
    public static final KKKeyBinding keyKaidoAttackB =
            new KKKeyBinding(
                    KAIDO_ATTACK_B_DESCRIPTION_KEY,
                    KeyConflictContext.IN_GAME,
                    InputMappings.Type.KEYSYM,
                    KAIDO_ATTACK_B_KEYCODE,
                    ATTACK_KEY_CATEGORY,
                    (clientPlayer) -> NetworkHandler.INSTANCE.sendToServer(new ServerboundSpecialAttackPacket(KaidoAttack.ATTACK_B)),
                    (clientPlayer) -> {},
                    (clientPlayer) -> {});

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
        if(key == keyKaidoMorph.getKey().getValue()){
            keyKaidoMorph.handleKey(clientPlayer);
        }
        if(key == keyDragonKaidoMorph.getKey().getValue()){
            keyDragonKaidoMorph.handleKey(clientPlayer);
        }
        if(key == keyKaidoAttackA.getKey().getValue()){
            keyKaidoAttackA.handleKey(clientPlayer);
        }
        if(key == keyKaidoAttackB.getKey().getValue()){
            keyKaidoAttackB.handleKey(clientPlayer);
        }
    }
}
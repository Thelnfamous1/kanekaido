package me.infamous.kanekaido.common.abilities;

import me.infamous.kanekaido.common.network.KeyBindAction;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.World;

import java.util.function.Consumer;

public enum KaidoAbility {

    FIREBALL(
            (serverPlayer) -> {
                World world = serverPlayer.level;

                Vector3d upVector = serverPlayer.getUpVector(1.0F);
                Quaternion upVectorQ = new Quaternion(new Vector3f(upVector), 0.0F, true);
                Vector3d viewVector = serverPlayer.getViewVector(1.0F);
                Vector3f viewVectorF = new Vector3f(viewVector);
                viewVectorF.transform(upVectorQ);

                double xA = viewVector.x * Constants.FIREBALL_VELOCITY;
                double yA = viewVector.y * Constants.FIREBALL_VELOCITY;
                double zA = viewVector.z * Constants.FIREBALL_VELOCITY;
                FireballEntity fireball = new FireballEntity(world, serverPlayer, xA, yA, zA);
                fireball.setPos(serverPlayer.getX(), serverPlayer.getY(0.5D) + 0.5D, serverPlayer.getZ());
                fireball.shoot(viewVectorF.x(), viewVectorF.y(), viewVectorF.z(), Constants.FIREBALL_VELOCITY, 0.0F);
                fireball.explosionPower = 1;
                world.addFreshEntity(fireball);
            },
            (serverPlayer) -> {
            },
            (serverPlayer) -> {
            }
    ),

    AIR_SLASH(
            (serverPlayer) -> {
    },
            (serverPlayer) -> {
    },
            (serverPlayer) -> {
    }
    ),

    ENERGY_BEAM(
            (serverPlayer) -> {
    },
            (serverPlayer) -> {
    },
            (serverPlayer) -> {
    }
    );
    private final Consumer<ServerPlayerEntity> onInitialPress;
    private final Consumer<ServerPlayerEntity> onHeld;
    private final Consumer<ServerPlayerEntity> onRelease;

    KaidoAbility(Consumer<ServerPlayerEntity> onInitialPress, Consumer<ServerPlayerEntity> onHeld, Consumer<ServerPlayerEntity> onRelease) {
        this.onInitialPress = onInitialPress;
        this.onHeld = onHeld;
        this.onRelease = onRelease;
    }

    public Consumer<ServerPlayerEntity> getHandlerForKeyBindAction(KeyBindAction keyBindAction) {
        switch (keyBindAction){
            case INITIAL_PRESS:
                return this.onInitialPress;
            case HELD:
                return this.onHeld;
            case RELEASE:
                return this.onRelease;
            default:
                return serverPlayerEntity -> {};
        }
    }

    private static class Constants {
        public static final float FIREBALL_VELOCITY = 1.6F;
    }
}
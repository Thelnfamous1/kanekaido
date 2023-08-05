package me.infamous.kanekaido.common.abilities;

import me.ichun.mods.morph.common.morph.MorphHandler;
import me.infamous.kanekaido.common.entities.EnergyBeam;
import me.infamous.kanekaido.common.logic.BeamColor;
import me.infamous.kanekaido.common.network.KeyBindAction;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import java.util.List;
import java.util.function.Consumer;

public enum KaidoAbility {

    FIREBALL(
            (serverPlayer) -> {
                LivingEntity activeEntity = getActiveEntity(serverPlayer);
                World world = activeEntity.level;

                Vector3d viewVector = activeEntity.getViewVector(1.0F);

                double xA = viewVector.x * Constants.FIREBALL_VELOCITY;
                double yA = viewVector.y * Constants.FIREBALL_VELOCITY;
                double zA = viewVector.z * Constants.FIREBALL_VELOCITY;
                FireballEntity fireball = new FireballEntity(world, serverPlayer, xA, yA, zA);
                fireball.getTags().add("kaido");
                fireball.setPos(activeEntity.getX(), getHeadY(activeEntity), activeEntity.getZ());
                fireball.shoot(viewVector.x(), viewVector.y(), viewVector.z(), Constants.FIREBALL_VELOCITY, 0.0F);
                fireball.explosionPower = 1;
                world.addFreshEntity(fireball);
            },
            doNothing(),
            doNothing()
    ),

    AIR_SLASH(
            doNothing(),
            doNothing(),
            doNothing()
    ),

    ENERGY_BEAM(
            (serverPlayer) -> {
                LivingEntity activeEntity = getActiveEntity(serverPlayer);
                World worldIn = activeEntity.level;

                EnergyBeam energyBeam = new EnergyBeam(Constants.BEAM_COLOR, worldIn, activeEntity);
                energyBeam.moveTo(getHeadX(activeEntity), getHeadY(activeEntity), getHeadZ(activeEntity), activeEntity.yRot, activeEntity.xRot);
                energyBeam.setOwner(activeEntity);
                worldIn.addFreshEntity(energyBeam);
    },
            doNothing(),
            (serverPlayer) -> {
                LivingEntity activeEntity = getActiveEntity(serverPlayer);
                List<EnergyBeam> beams = activeEntity.level.getEntitiesOfClass(EnergyBeam.class, activeEntity.getBoundingBox().inflate(1), energyBeam -> energyBeam.getOwner() == activeEntity);
                beams.forEach(Entity::remove);
    }
    );

    private static LivingEntity getActiveEntity(ServerPlayerEntity serverPlayer) {
        LivingEntity activeMorphEntity = MorphHandler.INSTANCE.getActiveMorphEntity(serverPlayer);
        return activeMorphEntity != null ? activeMorphEntity : serverPlayer;
    }

    private static Consumer<ServerPlayerEntity> doNothing() {
        return (serverPlayer) -> {
        };
    }

    private static double getHeadX(LivingEntity serverPlayer) {
        return serverPlayer.getX(0.5D) + 0.5D;
    }

    private static double getHeadY(LivingEntity serverPlayer) {
        return serverPlayer.getY(0.5D) + 0.5D;
    }

    private static double getHeadZ(LivingEntity serverPlayer) {
        return serverPlayer.getX(0.5D) + 0.5D;
    }

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
                return doNothing();
        }
    }

    private static class Constants {
        public static final float FIREBALL_VELOCITY = 1.6F;
        /**
         * See <a href="https://www.color-name.com/fire-yellow.color">Fire Yellow</a>
         */
        public static final BeamColor BEAM_COLOR = new BeamColor(
                (short)255, (short)255, (short)255,
                (short)254, (short)222, (short)23);
    }
}
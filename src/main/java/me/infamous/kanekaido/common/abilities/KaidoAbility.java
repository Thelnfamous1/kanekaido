package me.infamous.kanekaido.common.abilities;

import me.ichun.mods.morph.common.morph.MorphHandler;
import me.infamous.kanekaido.common.entities.EnergyBeam;
import me.infamous.kanekaido.common.logic.KaidoUtil;
import me.infamous.kanekaido.common.network.KeyBindAction;
import me.infamous.kanekaido.mixin.EntityAccess;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.World;

import java.util.List;
import java.util.function.Consumer;

public enum KaidoAbility {

    FIREBALL(
            (serverPlayer) -> {
                LivingEntity activeEntity = getActiveEntity(serverPlayer);
                World world = activeEntity.level;

                Vector3d viewVector = activeEntity.getViewVector(1.0F);
                for(int i = 0; i < 5; i++){
                    float angle = 0.0F;
                    boolean vertical = false;
                    switch (i){
                        case 1: {
                            angle = 10.0F;
                            break;
                        }
                        case 2: {
                            angle = -10.0F;
                            break;
                        }
                        case 3: {
                            angle = 10.0F;
                            vertical = true;
                            break;
                        }
                        case 4: {
                            angle = -10.0F;
                            vertical = true;
                            break;
                        }
                    }
                    Vector3d directionVector = vertical ? getSidewaysVector(activeEntity, 1.0F) : activeEntity.getUpVector(1.0F);
                    Quaternion upVectorQ = new Quaternion(new Vector3f(directionVector), angle, true);
                    Vector3f shotVector = new Vector3f(viewVector);
                    shotVector.transform(upVectorQ);

                    double xA = shotVector.x() * KaidoUtil.FIREBALL_VELOCITY;
                    double yA = shotVector.y() * KaidoUtil.FIREBALL_VELOCITY;
                    double zA = shotVector.z() * KaidoUtil.FIREBALL_VELOCITY;

                    FireballEntity fireball = new FireballEntity(world, serverPlayer, xA, yA, zA);
                    fireball.getTags().add(KaidoUtil.FIREBALL_TAG);
                    fireball.setPos(activeEntity.getX(), getHeadY(activeEntity), activeEntity.getZ());
                    fireball.shoot(shotVector.x(), shotVector.y(), shotVector.z(), KaidoUtil.FIREBALL_VELOCITY, 0.0F);
                    fireball.explosionPower = KaidoUtil.FIREBALL_EXPLOSION_POWER;
                    world.addFreshEntity(fireball);
                    activeEntity.level.playSound(null, activeEntity.blockPosition(), SoundEvents.GHAST_SHOOT, SoundCategory.PLAYERS, 10.0F, (activeEntity.getRandom().nextFloat() - activeEntity.getRandom().nextFloat()) * 0.2F + 1.0F);
                }
            },
            doNothing(),
            doNothing()
    ),

    AIR_SLASH(
            serverPlayer -> {
                LivingEntity activeEntity = getActiveEntity(serverPlayer);
                float width = activeEntity.getBbWidth();
                KaidoUtil.areaOfEffectAttack(width, width, activeEntity, KaidoUtil.AOE_KNOCKBACK_SCALE, KaidoUtil.AOE_DAMAGE_SCALE, ParticleTypes.SWEEP_ATTACK, SoundEvents.PLAYER_ATTACK_SWEEP, KaidoUtil.AOE_SWEEP_PARTICLE_COUNT, KaidoUtil.AIR_SLASH_Y_SHIFT);
            },
            doNothing(),
            doNothing()
    ),

    ENERGY_BEAM(
            (serverPlayer) -> {
                LivingEntity activeEntity = getActiveEntity(serverPlayer);
                World worldIn = activeEntity.level;

                EnergyBeam energyBeam = new EnergyBeam(KaidoUtil.BEAM_COLOR, worldIn, activeEntity);
                energyBeam.moveTo(getHeadX(activeEntity), getHeadY(activeEntity), getHeadZ(activeEntity), activeEntity.yRot, activeEntity.xRot);
                energyBeam.setOwner(activeEntity);
                energyBeam.setBeamWidth(KaidoUtil.ENERGY_BEAM_WIDTH);
                worldIn.addFreshEntity(energyBeam);
    },
            doNothing(),
            (serverPlayer) -> {
                LivingEntity activeEntity = getActiveEntity(serverPlayer);
                List<EnergyBeam> beams = activeEntity.level.getEntitiesOfClass(EnergyBeam.class, activeEntity.getBoundingBox().inflate(1), energyBeam -> energyBeam.getOwner() == activeEntity);
                beams.forEach(Entity::remove);
    }
    );

    private static Vector3d getSidewaysVector(LivingEntity entity, float partialTicks) {
        return ((EntityAccess)entity).callCalculateViewVector(entity.getViewXRot(partialTicks), entity.getViewYRot(partialTicks) - 90.0F);
    }

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
        return serverPlayer.getEyeY();
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

}
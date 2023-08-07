package me.infamous.kanekaido.common.logic;

import me.ichun.mods.morph.common.morph.MorphHandler;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.item.ArmorStandEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.*;
import net.minecraft.world.server.ServerWorld;

import java.util.UUID;

public class Util {

    public static final float AOE_KNOCKBACK_SCALE = 0.5F;
    public static final float AOE_DAMAGE_SCALE = 2.0F;

    public static DamageSource getMorphDamageSource(LivingEntity living) {
        UUID uuidOfPlayerForMorph = MorphHandler.INSTANCE.getUuidOfPlayerForMorph(living);
        DamageSource pSource;
        if(uuidOfPlayerForMorph != null){
            PlayerEntity player = living.level.getPlayerByUUID(uuidOfPlayerForMorph);
            pSource = DamageSource.playerAttack(player);
        } else{
            pSource = DamageSource.mobAttack(living);
        }
        return pSource;
    }

    public static void areaOfEffectAttack(double shiftScale, double inflateScale, LivingEntity attacker, float knockbackScale, float damageScale){
        float damage = (float) attacker.getAttributeValue(Attributes.ATTACK_DAMAGE);
        float knockback = (float) attacker.getAttributeValue(Attributes.ATTACK_KNOCKBACK);
        double xShift = (-MathHelper.sin(attacker.yBodyRot * ((float)Math.PI / 180F))) * shiftScale;
        double zShift = MathHelper.cos(attacker.yBodyRot * ((float)Math.PI  / 180F)) * shiftScale;
        for(LivingEntity target : attacker.level.getEntitiesOfClass(LivingEntity.class, attacker.getBoundingBox().move(xShift, 0.0D, zShift).inflate(inflateScale, 0.25D, inflateScale))) {
            if (target != attacker && !attacker.isAlliedTo(target) && (!(target instanceof ArmorStandEntity) || !((ArmorStandEntity) target).isMarker())) {
                target.knockback(knockback * knockbackScale, MathHelper.sin(attacker.yRot * ((float)Math.PI / 180F)), -MathHelper.cos(attacker.yRot * ((float)Math.PI / 180F)));
                DamageSource pSource = getMorphDamageSource(attacker);
                target.hurt(pSource, damage * damageScale);
            }
        }
        attacker.level.playSound(null, attacker.getX(), attacker.getY(), attacker.getZ(), SoundEvents.PLAYER_ATTACK_SWEEP, attacker.getSoundSource(), 1.0F, 1.0F);
        sweepAttack(attacker, shiftScale);
    }

    private static void sweepAttack(LivingEntity entity, double shiftScale) {
        double xShift = -MathHelper.sin(entity.yRot * ((float)Math.PI / 180F)) * shiftScale;
        double zShift = MathHelper.cos(entity.yRot * ((float)Math.PI / 180F)) * shiftScale;
        if (entity.level instanceof ServerWorld) {
            ((ServerWorld)entity.level).sendParticles(ParticleTypes.SWEEP_ATTACK,
                    entity.getX() + xShift,
                    entity.getY(0.5D),
                    entity.getZ() + zShift,
                    0,
                    xShift,
                    0.0D,
                    zShift,
                    0.0D);
        }
    }
}

package me.infamous.kanekaido.common.entities;

import me.infamous.kanekaido.common.logic.Util;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class Kaido extends CreatureEntity implements IAnimatable {
    public static final int ATTACK_A_LENGTH = 36;
    public static final int ATTACK_B_LENGTH = 40;
    private static final int ATTACK_A_POINT = 26;
    private static final int ATTACK_B_POINT = 20;

    public static final DataParameter<Integer> ATTACK_A_TIMER = EntityDataManager.defineId(Kaido.class,
            DataSerializers.INT);

    public static final DataParameter<Integer> ATTACK_B_TIMER = EntityDataManager.defineId(Kaido.class,
            DataSerializers.INT);

    AnimationFactory factory = new AnimationFactory(this);
    public Kaido(EntityType<? extends Kaido> type, World world) {
        super(type, world);
        this.maxUpStep = 3.0F;
    }

    public static AttributeModifierMap.MutableAttribute createAttributes() {
        return MonsterEntity.createMonsterAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.3F)
                .add(Attributes.MAX_HEALTH, 100.0D)
                .add(Attributes.ATTACK_DAMAGE, 15.0D)
                .add(Attributes.ATTACK_KNOCKBACK, 5.0D);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ATTACK_A_TIMER, 0);
        this.entityData.define(ATTACK_B_TIMER, 0);
    }

    @Override
    public void baseTick() {
        super.baseTick();

        tickDownAttackTimers();
    }

    public void tickDownAttackTimers() {
        int attackATimer = this.getAttackATimer();
        if (attackATimer > 0) {
            this.setAttackATimer(attackATimer - 1);
        }

        int attackBTimer = this.getAttackBTimer();
        if (attackBTimer > 0) {
            this.setAttackBTimer(attackBTimer - 1);
        }
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if(!this.level.isClientSide){
            if (this.getAttackATimer() == ATTACK_A_LENGTH - ATTACK_A_POINT) {
                float width = this.getBbWidth();
                Util.areaOfEffectAttack(width, width, this, Util.AOE_KNOCKBACK_SCALE, Util.AOE_DAMAGE_SCALE, ParticleTypes.EXPLOSION, SoundEvents.GENERIC_EXPLODE, 1);
            }
            if (this.getAttackBTimer() == ATTACK_B_LENGTH - ATTACK_B_POINT) {
                float width = this.getBbWidth();
                Util.areaOfEffectAttack(width, width, this, Util.AOE_KNOCKBACK_SCALE, Util.AOE_DAMAGE_SCALE, ParticleTypes.EXPLOSION, SoundEvents.GENERIC_EXPLODE, 1);
            }
        }
    }

    public boolean isAttacking(){
        return this.getAttackATimer() > 0 || this.getAttackBTimer() > 0;
    }

    public int getAttackATimer() {
        return this.entityData.get(ATTACK_A_TIMER);
    }

    public void setAttackATimer(int ticks) {
        this.entityData.set(ATTACK_A_TIMER, ticks);
    }

    public int getAttackBTimer() {
        return this.entityData.get(ATTACK_B_TIMER);
    }

    public void setAttackBTimer(int ticks) {
        this.entityData.set(ATTACK_B_TIMER, ticks);
    }

    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController<>(this, "controller", 5, this::predicate));
    }

    private <E extends Kaido> PlayState predicate(AnimationEvent<E> event) {
        if (this.getAttackATimer() > 0) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("kaidohybrid_attack1", false));
        } else if (this.getAttackBTimer() > 0) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("kaidohybrid_attack2", false));
        } else if (event.isMoving()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("kaidohybrid_walk", true));
        } else{
            event.getController().setAnimation(new AnimationBuilder().addAnimation("kaidohybrid_idle", true));
        }
        return PlayState.CONTINUE;
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }
}

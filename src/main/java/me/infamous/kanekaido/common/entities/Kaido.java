package me.infamous.kanekaido.common.entities;

import me.ichun.mods.morph.common.morph.MorphHandler;
import me.infamous.kanekaido.KaneKaido;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.item.ArmorStandEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.UUID;

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
                KaneKaido.LOGGER.info("Creating Attack A damage for {}", this);
                this.areaOfEffectAttack();
            }
            if (this.getAttackBTimer() == ATTACK_B_LENGTH - ATTACK_B_POINT) {
                this.areaOfEffectAttack();
            }
        }
    }

    private void areaOfEffectAttack(){
        float baseDamage = (float)this.getAttributeValue(Attributes.ATTACK_DAMAGE);
        float knockback = (float)this.getAttributeValue(Attributes.ATTACK_KNOCKBACK);
        double xShift = (-MathHelper.sin(this.yBodyRot * ((float)Math.PI / 180F))) * 3.0D;
        double zShift = MathHelper.cos(this.yBodyRot * ((float)Math.PI  / 180F)) * 3.0D;
        for(LivingEntity target : this.level.getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().move(xShift, 0.0D, zShift).inflate(3.0D, 0.25D, 3.0D))) {
            if (target != this && !this.isAlliedTo(target) && (!(target instanceof ArmorStandEntity) || !((ArmorStandEntity) target).isMarker())) {
                target.knockback(knockback * 0.5F, MathHelper.sin(this.yRot * ((float)Math.PI / 180F)), -MathHelper.cos(this.yRot * ((float)Math.PI / 180F)));
                DamageSource pSource = this.getMorphDamageSource();
                target.hurt(pSource, baseDamage * 2.0F);
            }
        }
    }

    private DamageSource getMorphDamageSource() {
        UUID uuidOfPlayerForMorph = MorphHandler.INSTANCE.getUuidOfPlayerForMorph(this);
        DamageSource pSource;
        if(uuidOfPlayerForMorph != null){
            PlayerEntity player = this.level.getPlayerByUUID(uuidOfPlayerForMorph);
            pSource = DamageSource.playerAttack(player);
        } else{
            pSource = DamageSource.mobAttack(this);
        }
        return pSource;
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
            event.getController().clearAnimationCache();
            return PlayState.STOP;
        }
        return PlayState.CONTINUE;
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }
}

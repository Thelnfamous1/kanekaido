package me.infamous.kanekaido.common.entities;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class Kaido extends CreatureEntity implements IAnimatable {

    public static final DataParameter<Integer> ATTACKING = EntityDataManager.defineId(Kaido.class,
            DataSerializers.INT);

    AnimationFactory factory = new AnimationFactory(this);
    public Kaido(EntityType<? extends Kaido> type, World world) {
        super(type, world);
    }

    public static AttributeModifierMap.MutableAttribute createAttributes() {
        return MonsterEntity.createMonsterAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.3F)
                .add(Attributes.ATTACK_DAMAGE, 3.0D)
                .add(Attributes.ARMOR, 2.0D);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ATTACKING, 0);
    }

    @Override
    public void baseTick() {
        super.baseTick();

        if (this.isAttacking() > 0) {
            this.setAttacking(this.isAttacking() - 1);
        }
    }

    public int isAttacking() {
        return this.entityData.get(ATTACKING);
    }

    public void setAttacking(int ticks) {
        this.entityData.set(ATTACKING, ticks);
    }

    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController<>(this, "controller", 5, this::predicate));
    }

    private <E extends Kaido> PlayState predicate(AnimationEvent<E> event) {
        if (this.isAttacking() > 0) {
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

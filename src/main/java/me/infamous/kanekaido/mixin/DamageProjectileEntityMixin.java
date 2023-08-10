package me.infamous.kanekaido.mixin;

import me.infamous.kanekaido.common.abilities.KaidoAbility;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.DamagingProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DamagingProjectileEntity.class)
public abstract class DamageProjectileEntityMixin extends ProjectileEntity {

    public DamageProjectileEntityMixin(EntityType<? extends ProjectileEntity> p_i231584_1_, World p_i231584_2_) {
        super(p_i231584_1_, p_i231584_2_);
    }

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;addParticle(Lnet/minecraft/particles/IParticleData;DDDDDD)V", shift = At.Shift.AFTER, ordinal = 1), method = "tick")
    private void kaidoParticle(CallbackInfo ci){
        if(this.getTags().contains(KaidoAbility.KaidoConstants.FIREBALL_TAG) && !this.level.isClientSide){
            Vector3d vector3d = this.getDeltaMovement();
            double d0 = this.getX() + vector3d.x;
            double d1 = this.getY() + vector3d.y;
            double d2 = this.getZ() + vector3d.z;
            ((ServerWorld)this.level).sendParticles(ParticleTypes.FLAME, d0, d1 + 0.5D, d2, 1, 0.0D, 0.0D, 0.0D, 0);
        }
    }
}

package me.infamous.kanekaido.common.entities;

import me.infamous.kanekaido.common.logic.BeamColor;
import me.infamous.kanekaido.common.network.NetworkHandler;
import me.infamous.kanekaido.common.network.ServerboundEnergyBeamPacket;
import me.infamous.kanekaido.common.registry.KKEntityTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.*;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class EnergyBeam extends Entity implements IEntityAdditionalSpawnData {
    public static final double MAX_RAYTRACE_DISTANCE = 256;
    public static final float BEAM_DAMAGE_PER_TICK = 0.5F; // 10.0F damage per second
    private LivingEntity owner;
    private UUID ownerUUID;
    private BeamColor beamColor;
    private float beamWidth = 0.2f;


    public EnergyBeam(EntityType<?> type, World world) {
        super(type, world);
    }


    public EnergyBeam(BeamColor beamColor, World world, LivingEntity owner) {
        this(KKEntityTypes.ENERGY_BEAM.get(), beamColor, world, owner);
    }


    public EnergyBeam(EntityType<?> type, BeamColor beamColor, World world, LivingEntity owner) {
        super(type, world);
        this.setOwner(owner);
        this.beamColor = beamColor;
    }

    public BeamColor getBeamColor() {
        return this.beamColor;
    }

    public void setOwner(LivingEntity owner) {
        this.owner = owner;
        if(owner != null){
            this.ownerUUID = owner.getUUID();
            updatePositionAndRotation();
        }
    }

    @Override
    public void tick() {
        LivingEntity owner = this.getOwner();
        if(!this.level.isClientSide) {
            if (owner == null || !owner.isAlive()) {
                this.remove();
                return;
            }
        }
        if (this.owner instanceof PlayerEntity && this.level.isClientSide()){
            updatePositionAndRotation();
            NetworkHandler.INSTANCE.sendToServer(new ServerboundEnergyBeamPacket(this));
        }else if (!(this.owner instanceof PlayerEntity) && !this.level.isClientSide()) {
            updatePositionAndRotation();
        }

        if(!this.level.isClientSide()){
            Set<LivingEntity> entities = new HashSet<>();
            AxisAlignedBB searchBox = new AxisAlignedBB(this.position(), this.position()).inflate(this.beamWidth);
            double distanceToDestination = beamTraceDistance(MAX_RAYTRACE_DISTANCE, 1.0f, false);
            double distanceTraveled = 0;
            while (!(this.position().distanceTo(searchBox.getCenter()) > distanceToDestination)
                    && !(this.position().distanceTo(searchBox.getCenter()) > MAX_RAYTRACE_DISTANCE)) {
                entities.addAll(this.level.getLoadedEntitiesOfClass(LivingEntity.class, searchBox,
                        nearbyEntity -> nearbyEntity != this.owner && nearbyEntity.isAlive()));
                distanceTraveled += 1.0D;
                Vector3d viewVector = this.getViewVector(1.0F);
                Vector3d targetVector = this.position().add(viewVector.x * distanceTraveled, viewVector.y * distanceTraveled, viewVector.z * distanceTraveled);
                searchBox = new AxisAlignedBB(targetVector, targetVector).inflate(1.0D);
            }
            for (LivingEntity entity : entities) {
                entity.invulnerableTime = 0;
                Vector3d deltaMovement = entity.getDeltaMovement();
                entity.hurt(DamageSource.indirectMagic(this.owner, this.owner), BEAM_DAMAGE_PER_TICK);
                entity.setDeltaMovement(deltaMovement);
            }
        }
    }

    public void updatePositionAndRotation() {
        LivingEntity owner = this.getOwner();
        Vector3d vec1 = owner.position();
        vec1 = vec1.add(this.getOffsetVector());
        this.setPos(vec1.x, vec1.y, vec1.z);
        this.yRot = boundDegrees(this.owner.yRot);
        this.xRot = boundDegrees(this.owner.xRot);
        this.yRotO = boundDegrees(this.owner.yRotO);
        this.xRotO = boundDegrees(this.owner.xRotO);
    }

    private float boundDegrees(float v){
        return (v % 360 + 360) % 360;
    }

    private Vector3d getOffsetVector() {
        Vector3d viewVector = this.getViewVector(1.0F);
        return new Vector3d(viewVector.x, getOwner().getEyeHeight()*0.8D, viewVector.z);
    }

    public float getBeamWidth() {
        return this.beamWidth;
    }

    public void setBeamWidth(float beamWidth){
        this.beamWidth = beamWidth;
    }

    public final Vector3d getWorldPosition(float partialTicks) {
        double x = MathHelper.lerp(partialTicks, this.xo, this.getX());
        double y = MathHelper.lerp(partialTicks, this.yo, this.getY());
        double z = MathHelper.lerp(partialTicks, this.zo, this.getZ());
        return new Vector3d(x, y, z);
    }

    public RayTraceResult beamTraceResult(double distance, float ticks, boolean passesWater) {
        Vector3d vector3d = this.getWorldPosition(ticks);
        Vector3d vector3d1 = this.getViewVector(ticks);
        Vector3d vector3d2 = vector3d.add(vector3d1.x * distance, vector3d1.y * distance, vector3d1.z * distance);
        return level.clip(new RayTraceContext(vector3d, vector3d2, RayTraceContext.BlockMode.COLLIDER, passesWater ? RayTraceContext.FluidMode.ANY : RayTraceContext.FluidMode.NONE, this));
    }

    public double beamTraceDistance(double distance, float ticks, boolean passesWater) {
        RayTraceResult rayTraceResult = beamTraceResult(distance, ticks, passesWater);
        double distanceToDestination = MAX_RAYTRACE_DISTANCE;
        if(rayTraceResult instanceof BlockRayTraceResult) {
            BlockPos collision = ((BlockRayTraceResult) rayTraceResult).getBlockPos();
            Vector3d destination = new Vector3d(collision.getX(), collision.getY(), collision.getZ());
            distanceToDestination = this.position().distanceTo(destination);
        }
        return distanceToDestination;
    }

    @Nullable
    public LivingEntity getOwner() {
        if(this.owner == null && this.ownerUUID != null){
            if(this.level instanceof ServerWorld) {
                Entity entity = ((ServerWorld) this.level).getEntity(this.ownerUUID);
                if (entity instanceof LivingEntity) {
                    this.owner = (LivingEntity) entity;
                }
            } else if(this.level.isClientSide) {
                this.owner = this.level.getPlayerByUUID(this.ownerUUID);
            }
        }
        return this.owner;
    }

    @Override
    protected void defineSynchedData() {

    }

    @Override
    protected void readAdditionalSaveData(CompoundNBT pCompound) {
        if (pCompound.hasUUID("Owner")) {
            this.ownerUUID = pCompound.getUUID("Owner");
        }
        this.beamColor = BeamColor.load(pCompound.getCompound("Color"));
    }

    @Override
    protected void addAdditionalSaveData(CompoundNBT pCompound) {
        if (this.ownerUUID != null) {
            pCompound.putUUID("Owner", this.ownerUUID);
        }
        pCompound.put("Color", this.getBeamColor().save(new CompoundNBT()));
    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void writeSpawnData(PacketBuffer buffer) {
        buffer.writeUUID(this.ownerUUID);
        buffer.writeShort(this.getBeamColor().getRedValue());
        buffer.writeShort(this.getBeamColor().getGreenValue());
        buffer.writeShort(this.getBeamColor().getBlueValue());
        buffer.writeShort(this.getBeamColor().getInnerRedValue());
        buffer.writeShort(this.getBeamColor().getInnerGreenValue());
        buffer.writeShort(this.getBeamColor().getInnerBlueValue());
    }

    @Override
    public void readSpawnData(PacketBuffer additionalData) {
        this.ownerUUID = additionalData.readUUID();
        this.beamColor = new BeamColor(
                additionalData.readShort(),
                additionalData.readShort(),
                additionalData.readShort(),
                additionalData.readShort(),
                additionalData.readShort(),
                additionalData.readShort()
        );
    }
}
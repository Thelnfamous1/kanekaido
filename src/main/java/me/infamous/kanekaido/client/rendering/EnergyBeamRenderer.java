package me.infamous.kanekaido.client.rendering;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import me.infamous.kanekaido.KaneKaido;
import me.infamous.kanekaido.common.entities.EnergyBeam;
import me.infamous.kanekaido.common.logic.BeamColor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Matrix3f;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.math.vector.Vector4f;

public class EnergyBeamRenderer<T extends EnergyBeam> extends EntityRenderer<T> {

    private static final float SPEED_MODIFIER = -0.02F;

    public EnergyBeamRenderer(EntityRendererManager manager) {
        super(manager);
    }

    @Override
    public ResourceLocation getTextureLocation(T energyBeam) {
        return new ResourceLocation(KaneKaido.MODID + ":textures/misc/beacon_beam_core.png");
    }

    @Override
    public void render(T energyBeam, float pEntityYaw, float pPartialTicks, MatrixStack pMatrixStack, IRenderTypeBuffer pBuffer, int pPackedLight) {
        double distance = energyBeam.beamTraceDistance(EnergyBeam.MAX_RAYTRACE_DISTANCE, 1.0F, false);

        drawBeams(distance, energyBeam, pPartialTicks, SPEED_MODIFIER, pMatrixStack);
    }

    private static void drawBeams(double distance, EnergyBeam energyBeam, float ticks, float speedModifier, MatrixStack pMatrixStack) {
        IVertexBuilder builder;
        long gameTime = energyBeam.level.getGameTime();
        double v = gameTime * speedModifier;
        float additiveThickness = (energyBeam.getBeamWidth() * 1.75F) * calculateLaserFlickerModifier(gameTime);

        BeamColor beamColor = energyBeam.getBeamColor();
        float r = beamColor.getRedValue() / 255F;
        float g = beamColor.getGreenValue() / 255F;
        float b = beamColor.getBlueValue() / 255F;
        float innerR = beamColor.getInnerRedValue() / 255F;
        float innerG = beamColor.getInnerGreenValue() / 255F;
        float innerB = beamColor.getInnerBlueValue() / 255F;
        IRenderTypeBuffer.Impl buffer = Minecraft.getInstance().renderBuffers().bufferSource();

        pMatrixStack.pushPose();
        pMatrixStack.mulPose(Vector3f.YP.rotationDegrees((MathHelper.lerp(ticks, boundDegrees(-energyBeam.yRot), boundDegrees(-energyBeam.yRotO)))));
        pMatrixStack.mulPose(Vector3f.XP.rotationDegrees(MathHelper.lerp(ticks, boundDegrees(energyBeam.xRot), boundDegrees(energyBeam.xRotO))));

        MatrixStack.Entry matrixstack$entry = pMatrixStack.last();
        Matrix3f matrixNormal = matrixstack$entry.normal();
        Matrix4f positionMatrix = matrixstack$entry.pose();

        //additive laser beam
        builder = buffer.getBuffer(MyRenderType.ENERGY_BEAM_GLOW);
        drawClosingBeam(builder, positionMatrix, matrixNormal, additiveThickness, distance / 10.0D, 0.5D, 1, ticks,
                innerR,innerG,innerB,0.9F);

        //main laser, colored part
        builder = buffer.getBuffer(MyRenderType.ENERGY_BEAM_MAIN);
        drawBeam(builder, positionMatrix, matrixNormal, energyBeam.getBeamWidth(), distance, v, v + distance * 1.5D, ticks,
                innerR,innerG,innerB, 0.7F);

        //core
        builder = buffer.getBuffer(MyRenderType.ENERGY_BEAM_CORE);
        drawBeam(builder, positionMatrix, matrixNormal, energyBeam.getBeamWidth() * 0.7F, distance, v, v + distance * 1.5D, ticks,
                r,g,b, 1.0F);
        pMatrixStack.popPose();
        buffer.endBatch();
    }

    private static float boundDegrees(float v){
        return (v % 360.0F + 360.0F) % 360.0F;
    }

    private static float calculateLaserFlickerModifier(long gameTime) {
        return 0.9F + 0.1F * MathHelper.sin(gameTime * 0.99F) * MathHelper.sin(gameTime * 0.3F) * MathHelper.sin(gameTime * 0.1F);
    }

    private static void drawBeam(IVertexBuilder builder, Matrix4f positionMatrix, Matrix3f matrixNormalIn, float thickness, double distance, double v1, double v2, float ticks, float r, float g, float b, float alpha) {
        Vector3f vector3f = new Vector3f(0.0F, 1.0F, 0.0F);
        vector3f.transform(matrixNormalIn);
        float xMin = -thickness;
        float xMax = thickness;
        float yMin = -thickness - 0.115F;
        float yMax = thickness - 0.115F;
        float zMin = 0;
        float zMax = (float) distance;

        Vector4f vec1 = new Vector4f(xMin, yMin, zMin, 1.0F);
        vec1.transform(positionMatrix);
        Vector4f vec2 = new Vector4f(xMin, yMin, zMax, 1.0F);
        vec2.transform(positionMatrix);
        Vector4f vec3 = new Vector4f(xMin, yMax, zMax, 1.0F);
        vec3.transform(positionMatrix);
        Vector4f vec4 = new Vector4f(xMin, yMax, zMin, 1.0F);
        vec4.transform(positionMatrix);
        drawQuad(builder, (float) v1, (float) v2, r, g, b, alpha, vector3f, vec1, vec2, vec3, vec4);

        vec1 = new Vector4f(xMax, yMin, zMin, 1.0F);
        vec1.transform(positionMatrix);
        vec2 = new Vector4f(xMax, yMin, zMax, 1.0F);
        vec2.transform(positionMatrix);
        vec3 = new Vector4f(xMax, yMax, zMax, 1.0F);
        vec3.transform(positionMatrix);
        vec4 = new Vector4f(xMax, yMax, zMin, 1.0F);
        vec4.transform(positionMatrix);
        drawQuad(builder, (float) v1, (float) v2, r, g, b, alpha, vector3f, vec1, vec2, vec3, vec4);

        vec1 = new Vector4f(xMin, yMax, zMin, 1.0F);
        vec1.transform(positionMatrix);
        vec2 = new Vector4f(xMin, yMax, zMax, 1.0F);
        vec2.transform(positionMatrix);
        vec3 = new Vector4f(xMax, yMax, zMax, 1.0F);
        vec3.transform(positionMatrix);
        vec4 = new Vector4f(xMax, yMax, zMin, 1.0F);
        vec4.transform(positionMatrix);
        drawQuad(builder, (float) v1, (float) v2, r, g, b, alpha, vector3f, vec1, vec2, vec3, vec4);

        vec1 = new Vector4f(xMin, yMin, zMin, 1.0F);
        vec1.transform(positionMatrix);
        vec2 = new Vector4f(xMin, yMin, zMax, 1.0F);
        vec2.transform(positionMatrix);
        vec3 = new Vector4f(xMax, yMin, zMax, 1.0F);
        vec3.transform(positionMatrix);
        vec4 = new Vector4f(xMax, yMin, zMin, 1.0F);
        vec4.transform(positionMatrix);
        drawQuad(builder, (float) v1, (float) v2, r, g, b, alpha, vector3f, vec1, vec2, vec3, vec4);
    }

    private static void drawClosingBeam(IVertexBuilder builder, Matrix4f positionMatrix, Matrix3f matrixNormalIn, float thickness, double distance, double v1, double v2, float ticks, float r, float g, float b, float alpha) {
        Vector3f vector3f = new Vector3f(0.0F, 1.0F, 0.0F);
        vector3f.transform(matrixNormalIn);

        float xMin = -thickness;
        float xMax = thickness;
        float yMin = -thickness - 0.115F;
        float yMax = thickness - 0.115F;
        float zMin = 0;
        float zMax = (float) distance;

        Vector4f vec1 = new Vector4f(xMin, yMin, zMin, 1.0F);
        vec1.transform(positionMatrix);
        Vector4f vec2 = new Vector4f(0, 0, zMax, 1.0F);
        vec2.transform(positionMatrix);
        Vector4f vec3 = new Vector4f(0, 0, zMax, 1.0F);
        vec3.transform(positionMatrix);
        Vector4f vec4 = new Vector4f(xMin, yMax, zMin, 1.0F);
        vec4.transform(positionMatrix);
        drawQuad(builder, (float) v1, (float) v2, r, g, b, alpha, vector3f, vec1, vec2, vec3, vec4);

        vec1 = new Vector4f(xMax, yMin, zMin, 1.0F);
        vec1.transform(positionMatrix);
        vec2 = new Vector4f(0, 0, zMax, 1.0F);
        vec2.transform(positionMatrix);
        vec3 = new Vector4f(0, 0, zMax, 1.0F);
        vec3.transform(positionMatrix);
        vec4 = new Vector4f(xMax, yMax, zMin, 1.0F);
        vec4.transform(positionMatrix);
        drawQuad(builder, (float) v1, (float) v2, r, g, b, alpha, vector3f, vec1, vec2, vec3, vec4);

        vec1 = new Vector4f(xMin, yMax, zMin, 1.0F);
        vec1.transform(positionMatrix);
        vec2 = new Vector4f(0, 0, zMax, 1.0F);
        vec2.transform(positionMatrix);
        vec3 = new Vector4f(0, 0, zMax, 1.0F);
        vec3.transform(positionMatrix);
        vec4 = new Vector4f(xMax, yMax, zMin, 1.0F);
        vec4.transform(positionMatrix);
        drawQuad(builder, (float) v1, (float) v2, r, g, b, alpha, vector3f, vec1, vec2, vec3, vec4);

        vec1 = new Vector4f(xMin, yMin, zMin, 1.0F);
        vec1.transform(positionMatrix);
        vec2 = new Vector4f(0, 0, zMax, 1.0F);
        vec2.transform(positionMatrix);
        vec3 = new Vector4f(0, 0, zMax, 1.0F);
        vec3.transform(positionMatrix);
        vec4 = new Vector4f(xMax, yMin, zMin, 1.0F);
        vec4.transform(positionMatrix);
        drawQuad(builder, (float) v1, (float) v2, r, g, b, alpha, vector3f, vec1, vec2, vec3, vec4);
    }

    private static void drawQuad(IVertexBuilder builder, float v1, float v2, float r, float g, float b, float alpha, Vector3f vector3f, Vector4f vec1, Vector4f vec2, Vector4f vec3, Vector4f vec4) {
        builder.vertex(vec4.x(), vec4.y(), vec4.z(), r, g, b, alpha, 0, v1, OverlayTexture.NO_OVERLAY, 15728880, vector3f.x(), vector3f.y(), vector3f.z());
        builder.vertex(vec3.x(), vec3.y(), vec3.z(), r, g, b, alpha, 0, v2, OverlayTexture.NO_OVERLAY, 15728880, vector3f.x(), vector3f.y(), vector3f.z());
        builder.vertex(vec2.x(), vec2.y(), vec2.z(), r, g, b, alpha, 1, v2, OverlayTexture.NO_OVERLAY, 15728880, vector3f.x(), vector3f.y(), vector3f.z());
        builder.vertex(vec1.x(), vec1.y(), vec1.z(), r, g, b, alpha, 1, v1, OverlayTexture.NO_OVERLAY, 15728880, vector3f.x(), vector3f.y(), vector3f.z());
    }
}
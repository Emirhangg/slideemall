package com.cwelth.slideemall.bakes;

import com.cwelth.slideemall.ModMain;
import com.cwelth.slideemall.blocks.BlockSlider;
import com.cwelth.slideemall.utils.EnumHoleTypes;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.model.pipeline.UnpackedBakedQuad;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.property.IExtendedBlockState;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import javax.vecmath.Matrix4f;
import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public abstract class BakedCubeModel implements IBakedModel {
    protected TextureAtlasSprite sprite;
    Function<ResourceLocation, TextureAtlasSprite> tGetter;
    protected VertexFormat format;
    IModelState state;

    public BakedCubeModel(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
        this.state = state;
        this.format = format;
        tGetter = bakedTextureGetter;
        sprite = tGetter.apply(new ResourceLocation(ModMain.MODID, "blocks/blockslider"));
    }

    private void putVertex(UnpackedBakedQuad.Builder builder, Vec3d normal, double x, double y, double z, float u, float v) {
        for (int e = 0; e < format.getElementCount(); e++) {
            switch (format.getElement(e).getUsage()) {
                case POSITION:
                    builder.put(e, (float)x, (float)y, (float)z, 1.0f);
                    break;
                case COLOR:
                    builder.put(e, 1.0f, 1.0f, 1.0f, 1.0f);
                    break;
                case UV:
                    if (format.getElement(e).getIndex() == 0) {
                        u = sprite.getInterpolatedU(u);
                        v = sprite.getInterpolatedV(v);
                        builder.put(e, u, v, 0f, 1f);
                        break;
                    }
                case NORMAL:
                    builder.put(e, (float) normal.x, (float) normal.y, (float) normal.z, 0f);
                    break;
                default:
                    builder.put(e);
                    break;
            }
        }
    }

    protected BakedQuad createQuad(Vec3d v1, Vec3d v2, Vec3d v3, Vec3d v4, TextureAtlasSprite sprite) {
        Vec3d normal = v3.subtract(v2).crossProduct(v1.subtract(v2)).normalize();

        UnpackedBakedQuad.Builder builder = new UnpackedBakedQuad.Builder(format);
        builder.setTexture(sprite);
        putVertex(builder, normal, v1.x, v1.y, v1.z, 0, 0);
        putVertex(builder, normal, v2.x, v2.y, v2.z, 0, 16);
        putVertex(builder, normal, v3.x, v3.y, v3.z, 16, 16);
        putVertex(builder, normal, v4.x, v4.y, v4.z, 16, 0);
        return builder.build();
    }

    @Override
    public boolean isAmbientOcclusion() {
        return true;
    }

    @Override
    public boolean isGui3d() {
        return true;
    }

    @Override
    public boolean isBuiltInRenderer() {
        return false;
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {
        return sprite;
    }

    @Override
    public ItemOverrideList getOverrides() {
        return ItemOverrideList.NONE;
    }

    @Override
    public Pair<? extends IBakedModel, Matrix4f> handlePerspective(ItemCameraTransforms.TransformType cameraTransformType) {
        switch(cameraTransformType) {
            case FIRST_PERSON_RIGHT_HAND:
            case FIRST_PERSON_LEFT_HAND:
            case THIRD_PERSON_RIGHT_HAND:
            case THIRD_PERSON_LEFT_HAND:
                return Pair.of(this, new Matrix4f(new Quat4f(0, 1, 2, 0), new Vector3f(0, 0, 0), 0.5F));
            case FIXED:
                return Pair.of(this, new Matrix4f(new Quat4f(0, 0, 0, 1), new Vector3f(0, -.2F, 0), 0.5F));
            case GROUND:
            case GUI:
                return Pair.of(this, new Matrix4f(new Quat4f(0, -0.5F, 0.85F, 1), new Vector3f(0, 0, 0), 0.6875F));
            default:
                return IBakedModel.super.handlePerspective(cameraTransformType);
        }
    }
}

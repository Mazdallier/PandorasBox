/*
 * Copyright (c) 2014, Lukas Tenbrink.
 * http://lukas.axxim.net
 */

package ivorius.pandorasbox.client;

import ivorius.pandorasbox.PandorasBox;
import ivorius.pandorasbox.client.rendering.effects.PBEffectRenderer;
import ivorius.pandorasbox.client.rendering.effects.PBEffectRenderingRegistry;
import ivorius.pandorasbox.effects.PBEffect;
import ivorius.pandorasbox.entitites.EntityPandorasBox;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

/**
 * Created by lukas on 30.03.14.
 */
public class RenderPandorasBox extends Render
{
    public ModelBase model;
    public ResourceLocation texture;

    public RenderPandorasBox()
    {
        model = new ModelPandorasBox();
        texture = new ResourceLocation(PandorasBox.MODID, PandorasBox.filePathTextures + "pbTexture.png");
    }

    @Override
    public void doRender(Entity entity, double x, double y, double z, float yaw, float partialTicks)
    {
        EntityPandorasBox entityPandorasBox = (EntityPandorasBox) entity;

        GL11.glPushMatrix();
        GL11.glTranslated(x, y + MathHelper.sin((entity.ticksExisted + partialTicks) * 0.04f) * 0.05, z);
        GL11.glRotatef(-yaw, 0.0F, 1.0F, 0.0F);

        PBEffect effect = entityPandorasBox.getBoxEffect();
        if (!effect.isDone(entityPandorasBox, entityPandorasBox.getEffectTicksExisted()) && entityPandorasBox.getDeathTicks() < 0)
        {
            PBEffectRenderer renderer = PBEffectRenderingRegistry.rendererForEffect(effect);
            if (renderer != null)
                renderer.renderBox(entityPandorasBox, effect, partialTicks);
        }

        if (!entity.isInvisible())
        {
            float boxScale = entityPandorasBox.getCurrentScale();
            if (boxScale < 1.0f)
                GL11.glScalef(boxScale, boxScale, boxScale);

            GL11.glTranslatef(0.0f, 1.5f, 0.0f);
            GL11.glRotatef(180.0f, 0.0f, 0.0f, 1.0f);

            EntityArrow emptyEntity = new EntityArrow(entity.worldObj);
            emptyEntity.rotationPitch = entityPandorasBox.getRatioBoxOpen(partialTicks) * 120.0f / 180.0f * 3.1415926f;
            bindEntityTexture(entity);
            model.render(emptyEntity, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
        }

        GL11.glPopMatrix();
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity var1)
    {
        return texture;
    }
}

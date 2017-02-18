package com.forgescriptcompiler.proxy;

import com.forgescriptcompiler.entity.EntityRobot;
import com.forgescriptcompiler.render.RenderRobot;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderManager;

public class ClientProxy extends ServerProxy
{
    public void registerModels()
    {
        RenderManager manager = Minecraft.getMinecraft().getRenderManager();
        manager.entityRenderMap.put(EntityRobot.class, new RenderRobot(manager));
    }

    @Override
    public void preInit()
    {
    }
}
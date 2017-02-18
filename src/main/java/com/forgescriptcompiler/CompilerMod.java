package com.forgescriptcompiler;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.forgescriptcompiler.command.CommandRun;
import com.forgescriptcompiler.entity.EntityRobot;
import com.forgescriptcompiler.proxy.ServerProxy;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.EntityRegistry;

@Mod(modid = CompilerMod.MODID, version = CompilerMod.VERSION)
public class CompilerMod
{
    public static final String MODID = "compilermod";
    public static final String VERSION = "1.0";
    
    public static final String SCRIPTS_LOCATION = System.getProperty("user.home") + File.separator + "Scripts" + File.separator;

    public static final String ROBOT_TEXTURE_LOCATION = "textures/entity/robot.png";
    
    public static final String[] NAMES = new String[] {"Anders", "Andreas", "Matias", "Simon", "Theis", "Thomas"};
    
    public static final List<UUID> CHILD_ENTITIES = new ArrayList<UUID>();

    @Mod.Instance(MODID)
    public static CompilerMod Instance;

    @SidedProxy(clientSide = "com.forgescriptcompiler.proxy.ClientProxy", serverSide = "com.forgescriptcompiler.proxy.ServerProxy")
    public static ServerProxy Proxy;
    
    @EventHandler
    public void preinit(FMLPreInitializationEvent event)
    {
        Proxy.preInit();
        EntityRegistry.registerModEntity(new ResourceLocation(MODID, ROBOT_TEXTURE_LOCATION), EntityRobot.class, "EntityTest", 255, Instance, 64, 1, true);
    }
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        Proxy.registerModels();
    }
    
    @EventHandler
    public void serverStart(FMLServerStartingEvent event)
    {
        event.registerServerCommand(new CommandRun());
    }
}
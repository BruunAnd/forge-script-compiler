package com.forgescriptcompiler.command;

import java.util.ArrayList;
import java.util.List;

import com.forgescriptcompiler.CompilerMod;
import com.forgescriptcompiler.ScriptBase;
import com.forgescriptcompiler.ScriptRunner;
import com.forgescriptcompiler.compiler.CustomJavaCompiler;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

public class CommandRun implements ICommand
{
    private final List<String> Aliases;
    
    public CommandRun()
    {
        Aliases = new ArrayList<String>();
        Aliases.add("run");
        Aliases.add("runscript");
    }
    
    @Override
    public int compareTo(ICommand arg0)
    {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public String getName()
    {
        return "run";
    }

    @Override
    public String getUsage(ICommandSender sender)
    {
        return "/run <name> [count]";
    }

    @Override
    public List<String> getAliases()
    {
        return Aliases;
    }
    
    
    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        if (args.length < 1) return;
        final String scriptName = args[0];
        int executionTimes = 1;
        if (args.length > 1)
            executionTimes = Integer.parseInt(args[1]);
        
        try
        {
            Class<?> compiledClass = new CustomJavaCompiler().compile(CompilerMod.SCRIPTS_LOCATION, scriptName);
            for (int i = 0; i < executionTimes; i++)
            {
                ScriptBase script = (ScriptBase) compiledClass.newInstance();
                script.init(server.getEntityWorld(), (EntityPlayer) sender);
                new ScriptRunner(script).start();
            }
        }
        catch (Exception e)
        {
            sender.sendMessage(new TextComponentString(TextFormatting.RED + "[Error]" + TextFormatting.RESET + " " + e.getMessage()));

            e.printStackTrace();

            return;
        }
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender)
    {
        return true;
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos targetPos)
    {
        return null;
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index)
    {
        return false;
    }

}

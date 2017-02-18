package com.forgescriptcompiler;

import com.forgescriptcompiler.entity.EntityRobot;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public abstract class ScriptBase
{
    protected World _world;
    protected EntityRobot _scriptedEntity;
    protected EntityPlayer _player;

    public void init(World world, EntityPlayer player)
    {
        this._world = world;
        this._player = player;

        _scriptedEntity = new EntityRobot(_world, player);
        BlockPos spawnPosition = this._player.getPosition();
        do
        {
        	this._scriptedEntity.setPosition(spawnPosition.getX() + world.rand.nextInt(5), spawnPosition.getY(), spawnPosition.getZ() + world.rand.nextInt(5));
        	
            switch (_world.rand.nextInt() % 4)
            {
            case 0:
                spawnPosition = spawnPosition.west();
                break;
            case 1:
                spawnPosition = spawnPosition.east();
                break;
            case 2:
                spawnPosition = spawnPosition.north();
                break;
            case 3:
                spawnPosition = spawnPosition.south();
                break;
            }
        } while (!this._scriptedEntity.getCanSpawnHere());
        this._world.spawnEntity(_scriptedEntity);
    }

    protected synchronized Command queueAndWait()
    {
        try
        {
            Command newCommand = new Command();
            this._scriptedEntity.CommandQueue.add(newCommand);
            newCommand.waitForCanExecute();
            return newCommand;
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        return null;
    }
    
    protected void setWorldTime(long time)
    {
        Command command = queueAndWait();
        
        _world.setWorldTime(time % 24000);
        
        command.setHasBeenExecuted(true);
    }

    protected void notify(String notifyString)
    {
        Command command = queueAndWait();
        
        _player.sendMessage(new TextComponentString(_scriptedEntity.getFormatting() + "[" + _scriptedEntity.getName() + "]" + " " + TextFormatting.RESET + " " + notifyString));
        
        command.setHasBeenExecuted(true);
    }

    protected void selfDestruct()
    {
        explode(0.0F);
        removeEntity();
    }
    
    protected void explode(float strength)
    {
        Command command = queueAndWait();
        
        _world.createExplosion(this._scriptedEntity, getX(), getY(), getZ(), strength, this._world.getGameRules().getBoolean("mobGriefing"));
        
        command.setHasBeenExecuted(true);
    }

    protected void removeEntity()
    {
        Command command = queueAndWait();
        
        _world.removeEntity(_scriptedEntity);
        
        command.setHasBeenExecuted(true);
    }
    
    protected int getX()
    {
        return (int) _scriptedEntity.posX;
    }
    
    protected int getY()
    {
        return (int) _scriptedEntity.posY;
    }
    
    protected int getZ()
    {
        return (int) _scriptedEntity.posZ;
    }
    
    protected BlockPos getOwnerPosition()
    {
        return _player.getPosition();
    }
    
    protected int distanceToOwner()
    {
    	return (int) _scriptedEntity.getDistanceToEntity(_player);
    }
    
    protected synchronized void navigateToOwner()
    {
        navigateToBlock(_player.getPosition());
    }
    
    protected synchronized void navigateToBlock(BlockPos pos)
    {
        Command command = queueAndWait();
        _scriptedEntity.getNavigator().clearPathEntity();
        _scriptedEntity.getNavigator().setPath(_scriptedEntity.getNavigator().getPathToPos(pos), 0.6D);
        command.setHasBeenExecuted(true);
        
        try
        {
            wait(500);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }
    
    protected void move(char direction)
    {
        BlockPos targetPosition = _scriptedEntity.getPosition();
        switch (direction)
        {
        case 'n':
            targetPosition = targetPosition.north();
            break;
        case 's':
            targetPosition = targetPosition.south();
            break;
        case 'e':
            targetPosition = targetPosition.east();
            break;
        case 'w':
            targetPosition = targetPosition.west();
            break;
        }
        
        navigateToBlock(targetPosition);
    }
    
    protected void mine(char direction)
    {
        BlockPos targetBlockPosition = _scriptedEntity.getPosition();
        switch (direction)
        {
        case 'd':
            targetBlockPosition = targetBlockPosition.down();
            break;
        case 'u':
            targetBlockPosition = targetBlockPosition.up();
            break;
        case 'n':
            faceAndMine((targetBlockPosition = targetBlockPosition.north()));
            break;
        case 's':
            faceAndMine((targetBlockPosition = targetBlockPosition.south()));
            break;
        case 'e':
            faceAndMine((targetBlockPosition = targetBlockPosition.east()));
            break;
        case 'w':
            faceAndMine((targetBlockPosition = targetBlockPosition.west()));
            break;
        default:
            return;
        }
        
        if (direction != 'u' && direction != 'd')
            targetBlockPosition = targetBlockPosition.up();
        
        faceAndMine(targetBlockPosition);
    }
    
    private void faceAndMine(BlockPos position)
    {
        if (_world.isAirBlock(position))
            return;
        
        // face(position);
        mineBlock(position);
    }
    
    private synchronized void mineBlock(BlockPos position)
    {
        int waitingTime = 1;
        Command command = queueAndWait();

        if (_world.destroyBlock(position, true))
            waitingTime = 500;
        
        command.setHasBeenExecuted(true);
        
        try
        {
            wait(waitingTime);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }
    
    public abstract void run();
}

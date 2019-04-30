package net.axiiom.spigotplugins;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.material.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class ChestSigns implements Listener
{
    @EventHandler
    public void onSignPlaced(BlockPlaceEvent _event)
    {
        Player player = _event.getPlayer();
        Block placed = _event.getBlockPlaced();

        if(Utils.isWallSign(placed.getType()))
        {
            player.sendMessage("Wall sign placed!");
            Sign sign = (Sign) placed.getState().getData();
            Block attached_block = placed.getRelative(sign.getAttachedFace());

            //FOR DEBUG
            if(attached_block.getType().equals(Material.CHEST))
                player.sendMessage("ITS ATTACHED TO A CHEST.");
        }
    }
}

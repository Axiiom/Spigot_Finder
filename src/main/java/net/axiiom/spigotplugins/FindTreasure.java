package net.axiiom.spigotplugins;

import com.mysql.jdbc.Util;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.material.Sign;

public class FindTreasure
{
    private static int MAX_BUILD_HEIGHT = 256;

    public static boolean findChest(Player _player)
    {
        Location loc = _player.getLocation();
        Finder finder = new Finder(loc);

        if(!finder.find(loc, new int[][]{{5,5},{5,5},{5,5}}, Material.CHEST))
            return false;

        Block found = finder.getFoundItems().get(0).getBlock();
        BlockFace bf = signAttachedTo(found);

        if(bf == null) {
            _player.sendMessage("Chest at: " + found.getLocation().getX() + "/" + found.getLocation().getY() + "/" + found.getLocation().getZ()
                + " does not contain a sign.");
        } else {
            _player.sendMessage("Sign attached to chest at: " + bf.toString());
        }

        return true;
    }

    private static BlockFace signAttachedTo(Block placed)
    {
        for(BlockFace b : new BlockFace[]{BlockFace.NORTH, BlockFace.SOUTH, BlockFace.EAST, BlockFace.WEST})
        {
            if(Utils.isWallSign(placed.getRelative(b).getType())
                && placed.getRelative(b).getState() instanceof org.bukkit.block.Sign)
            {
                org.bukkit.block.Sign sign = (org.bukkit.block.Sign)placed.getRelative(b).getState();
                BlockData bd = sign.getBlockData();

                Sign s = (Sign) placed.getRelative(b).getState().getData();
                Block attached_block = placed.getRelative(b).getRelative(s.getAttachedFace());

                if (attached_block.equals(placed))
                    return b;
            }
        }

        return null;
    }


    public static boolean findTreasure(Player player, Material block, int[][] area, int bounds, int spawn_layer)
    {
        final String BLOCK_TYPE = block.toString().toLowerCase().replaceAll("_", " ");
        final String NO_BLOCK_FOUND_MSG = ChatColor.GRAY + "There are no " + ChatColor.GOLD + BLOCK_TYPE
                + ChatColor.GRAY + "(s) near you.";

        if(!isInBounds(area, bounds)) {
            player.sendMessage(ChatColor.RED + "Search area out of bounds. Max Range: " + bounds);
            return true;
        }

        if(!isWithinSpawnLayer(player.getLocation(), area[1], spawn_layer)) {
            player.sendMessage(ChatColor.RED + NO_BLOCK_FOUND_MSG);
            player.sendMessage(ChatColor.GRAY + "(Try getting closer to its spawn layer)");
            return true;
        }

        if(area[1][0] > spawn_layer)
            area[1][0] = spawn_layer;
        if(area[1][1] < 0)
            area[1][1] = 0;

        Location current_location = player.getLocation();
        Finder finder;

        try {
            finder = new Finder(current_location);
        } catch(Exception e){
            player.getServer().getLogger().info ("Finder could not find this player!!");
            System.out.println(e.getClass().toString());
            return false;
        }

        boolean bBlocks_found = finder.find(current_location, area, block);
        if (!bBlocks_found) {
            player.sendMessage(NO_BLOCK_FOUND_MSG);
            return true;
        }

        Location closest = finder.getClosest(current_location);
        if (player.getInventory().contains(Material.COMPASS)) {
            player.setCompassTarget(closest);
            player.sendMessage(ChatColor.AQUA + "" + ChatColor.ITALIC + "The compass points the way...");
        }

        String coords_of_closest = (int) closest.getX() + "/" + (int) closest.getY() + "/" + (int) closest.getZ();
        String output_num_found  = ChatColor.GRAY + "There are " + ChatColor.YELLOW + finder.getFoundItems().size()
                + " " + ChatColor.GOLD + BLOCK_TYPE + ChatColor.GRAY + "(s)" + ChatColor.GRAY + " near you.";
        String output_closest    = ChatColor.GRAY + "Closest: " + ChatColor.YELLOW + coords_of_closest;

        player.sendMessage(output_num_found);
        player.sendMessage(output_closest);


        return true;
    }

    public static boolean findTreasure(Player player, Material block, int[][] area, int bounds) {
        return findTreasure(player,block,area,bounds,MAX_BUILD_HEIGHT);
    }

    private static boolean isWithinSpawnLayer(Location player_location, int[] y_rng, int spawn_layer) {
        return player_location.getY() - y_rng[1] < spawn_layer;
    }

    private static boolean isInBounds(int[][] area, int bounds)
    {
        int sum = 0;
        for(int row = 0; row < area.length; row++)
        {
            for(int col = 0; col < area[0].length; col++)
            {
                sum += area[row][col];
            }
        }

        bounds *= 6;
        return sum <= bounds;
    }

}
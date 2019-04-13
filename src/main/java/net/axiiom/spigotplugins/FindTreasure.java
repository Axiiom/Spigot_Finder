package net.axiiom.spigotplugins;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class FindTreasure
{
    private static int MAX_BUILD_HEIGHT = 256;

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
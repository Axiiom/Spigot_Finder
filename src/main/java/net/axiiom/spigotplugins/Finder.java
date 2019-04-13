package net.axiiom.spigotplugins;

import org.bukkit.Location;
import org.bukkit.Material;

import java.util.ArrayList;
public class Finder
{
    private Location seeker;
    private ArrayList<Location> found_items = new ArrayList<>();
    private int split_index;
    private boolean sorted;

    public Finder(Location seeker) {
        reset(seeker);
    }

    private void reset(Location seeker) {
        this.seeker = seeker;
        found_items.clear();
        split_index = 0;
        sorted = false;
    }

    public Location getSeeker() {
        return seeker;
    }

    public ArrayList<Location> getFoundItems() {
        return found_items;
    }

    public void clear() {
        reset(seeker);
    }


    public boolean find(Location seeker, int[][] area, Material hider)
    {
        int list_size_before = found_items.size();

        int seekerX = (int)seeker.getX();
        int seekerY = (int)seeker.getY();
        int seekerZ = (int)seeker.getZ();

        int[] x_rng = new int[]{area[0][0], area[0][1]};
        int[] y_rng = new int[]{area[1][0], area[1][1]};
        int[] z_rng = new int[]{area[2][0], area[2][1]};

        int[] x_bound = new int[]{ seekerX + x_rng[0], seekerX - x_rng[1]}; //EAST WEST
        int[] y_bound = new int[]{ seekerY + y_rng[0], seekerY - y_rng[1]}; //UP DOWN
        int[] z_bound = new int[]{ seekerZ + z_rng[0], seekerZ - z_rng[1]}; //NORTH SOUTH

        for(int x_pos = x_bound[1]; x_pos <= x_bound[0]; x_pos++)
        {
            for(int y_pos = y_bound[1]; y_pos <= y_bound[0]; y_pos++)
            {
                for(int z_pos = z_bound[1]; z_pos <= z_bound[0]; z_pos++)
                {
                    Location cur = new Location(seeker.getWorld(),x_pos,y_pos,z_pos);
                    if(cur.getBlock().getType().equals(hider))
                        found_items.add(cur);
                }

            }
        }

        return list_size_before < found_items.size();
    }

    public Location getClosest(Location l) {
        double dist = distance(found_items.get(0), seeker);
        Location closest = null;

        for(Location block : found_items) {
            double temp_dist = distance(block, seeker);
            if (temp_dist <= dist) {
                dist = temp_dist;
                closest = block;
            }
        }

        return closest;
    }

    private static double distance(Location a, Location b) {
        double x = Math.pow(a.getX()-b.getX(),2);
        double y = Math.pow(a.getY()-b.getY(),2);
        double z = Math.pow(a.getZ()-b.getZ(),2);

        return Math.sqrt(x+y+z);
    }

}
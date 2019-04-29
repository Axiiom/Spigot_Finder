package net.axiiom.spigotplugins;

import org.bukkit.Material;

public class Utils
{
    public static boolean isWallSign(Material _material)
    {
        switch(_material)
        {
            case ACACIA_WALL_SIGN: case BIRCH_WALL_SIGN:
            case DARK_OAK_WALL_SIGN: case JUNGLE_WALL_SIGN:
            case OAK_WALL_SIGN: case SPRUCE_WALL_SIGN:
                return true;
            default:
                return false;
        }
    }
}

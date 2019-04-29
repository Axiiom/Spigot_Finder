package net.axiiom.spigotplugins;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class FinderPlugin extends JavaPlugin implements CommandExecutor {

    private int diamond_bounds = 50;
    private int spawner_bounds = 50;
    private boolean enabled = true;

    @Override
    public void onEnable() {
        // main startup logic
        setupConfig();
        getLogger().info("Finder Enabled!");


        ChestSigns chestlistener = new ChestSigns();
        getServer().getPluginManager().registerEvents(chestlistener, this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if(enabled) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("You must be a player to use this command.");
                return true;
            }

            Player player = (Player) sender;
            if (command.getName().equalsIgnoreCase("treasure")) {
                int[][] range = getRange(diamond_bounds, args);
                return FindTreasure.findTreasure(player, Material.DIAMOND_ORE, range, diamond_bounds, 11);

            } else if (command.getName().equalsIgnoreCase("spawner")) {
                int[][] range = getRange(spawner_bounds, args);
                return FindTreasure.findTreasure(player, Material.SPAWNER, range, spawner_bounds);
            } else if(command.getName().equalsIgnoreCase("chest")) {
                return FindTreasure.findChest(player);
            }

            return false;
        }

        return true;
    }

    private int[][] getRange(int bounds, String[] args) {
        int[] x_rng = new int[]{bounds/2,bounds/2};
        int[] y_rng = new int[]{bounds/2,bounds/2};
        int[] z_rng = new int[]{bounds/2,bounds/2};

        if (args.length == 1) {
            x_rng[0] = x_rng[1] = Integer.parseInt(args[0]);
            y_rng[0] = y_rng[1] = Integer.parseInt(args[0]);
            z_rng[0] = z_rng[1] = Integer.parseInt(args[0]);
        } else if (args.length == 3) {
            x_rng[0] = x_rng[1] = Integer.parseInt(args[0]);
            y_rng[0] = y_rng[1] = Integer.parseInt(args[1]);
            z_rng[0] = z_rng[1] = Integer.parseInt(args[2]);
        }

        return new int[][]{x_rng,y_rng,z_rng};
    }

    private void setupConfig() {
        getConfig().options().copyDefaults(true);

        spawner_bounds = getConfig().getInt("max-range.spawner");
        diamond_bounds = getConfig().getInt("max-range.diamond_ore");
        enabled = getConfig().getBoolean("finder-enabled");

        saveConfig();
    }
}
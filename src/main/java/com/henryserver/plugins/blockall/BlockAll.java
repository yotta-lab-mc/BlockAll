package com.henryserver.plugins.blockall;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class BlockAll extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getLogger().info("プラグインが開始しました");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("プラグインが停止しました");
    }

    @EventHandler
    public void onPlayerJoin(BlockBreakEvent e) {
        Block b = e.getBlock();

        getLogger().info("Player has broken: " + e.getBlock());

        if (b.getType() == Material.GRASS) {
            e.setCancelled(true);
            b.setType(Material.SAND);
        }
    }
}

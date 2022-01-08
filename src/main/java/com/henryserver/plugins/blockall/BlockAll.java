package com.henryserver.plugins.blockall;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.function.Supplier;

public final class BlockAll extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getServer().getPluginManager().registerEvents(this, this);
        getLogger().info("プラグインが開始しました");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("プラグインが停止しました");
    }

    @EventHandler
    public void breakLog(BlockBreakEvent e) {

        int maxBreakRange = 5;

        Block block = e.getBlock();
        Player player = e.getPlayer();

        getLogger().info("Player has broken: " + e.getBlock());

        Material[] acceptBlocks = {
                Material.ACACIA_LOG,
                Material.BIRCH_LOG,
                Material.JUNGLE_LOG,
                Material.DARK_OAK_LOG,
                Material.OAK_LOG,
                Material.SPRUCE_LOG,
        };

        if (!Arrays.asList(acceptBlocks).contains(block.getType())) return;

        Location posBlock = block.getLocation();
        Collection<ItemStack> droppedItemStack = this.breakChainedBlocks(player.getWorld(), block.getType(), posBlock);
        if (droppedItemStack == null) {
            return;
        }

        for (ItemStack item: droppedItemStack) {
            e.getPlayer().getWorld().dropItem(block.getLocation(), item);
        }

    }

    private Collection<ItemStack> breakChainedBlocks(World world, Material targetMaterial, Location blockPosition) {

        Block block = world.getBlockAt(blockPosition);

        if (!block.getType().equals(targetMaterial)) {
            return null;
        }

        Collection<ItemStack> drops = block.getDrops();
        Collection<ItemStack> droppedItemStack = new ArrayList<>(drops);

        block.setType(Material.AIR);
        Collection<ItemStack> itemStack = null;

        // next to X
        Collection<ItemStack> itemStack1 = breakChainedBlocks(world, targetMaterial, (blockPosition.clone()).add( 1, 0, 0));
        if (itemStack1 != null) droppedItemStack.addAll(itemStack1);
        Collection<ItemStack> itemStack2 = breakChainedBlocks(world, targetMaterial, (blockPosition.clone()).add( -1, 0, 0));
        if (itemStack2 != null) droppedItemStack.addAll(itemStack2);

        // next to Y
        Collection<ItemStack> itemStack3 = breakChainedBlocks(world, targetMaterial, (blockPosition.clone()).add( 0, 1, 0));
        if (itemStack3 != null) droppedItemStack.addAll(itemStack3);
        Collection<ItemStack> itemStack4 = breakChainedBlocks(world, targetMaterial, (blockPosition.clone()).add( 0, -1, 0));
        if (itemStack4 != null) droppedItemStack.addAll(itemStack4);

        // next to Z
        Collection<ItemStack> itemStack5 = breakChainedBlocks(world, targetMaterial, (blockPosition.clone()).add( 0, 0, 1));
        if (itemStack5 != null) droppedItemStack.addAll(itemStack5);
        Collection<ItemStack> itemStack6 = breakChainedBlocks(world, targetMaterial, (blockPosition.clone()).add( 0, 0, -1));
        if (itemStack6 != null) droppedItemStack.addAll(itemStack6);

        return droppedItemStack;
    }
}

package net.z4te;

import net.md_5.bungee.api.ChatMessageType;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.UUID;
import net.md_5.bungee.api.chat.TextComponent;
import org.jetbrains.annotations.NotNull;

public final class Main extends JavaPlugin {

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss VV");
    private final HashSet<UUID> toggledPlayers = new HashSet<>();
    @Override
    public void onEnable() {
        // Plugin startup logic

        new BukkitRunnable() {
            @Override
            public void run() {
                ZonedDateTime now = ZonedDateTime.now();
                String timeString = now.format(TIME_FORMATTER);
                TextComponent component = new TextComponent();

                component.setText(timeString);

                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (toggledPlayers.contains(player.getUniqueId())) {
                        player.spigot().sendMessage(ChatMessageType.ACTION_BAR,component);
                    }
                }
            }
        }.runTaskTimer(this, 0, 20);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic

    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, Command command, @NotNull String label, String[] args) {

        Player player = (Player) sender;
        String prefix = "[YYBR Clock] ";

        if (command.getName().equalsIgnoreCase("clock")) {
            if (args.length == 0) {
                UUID playerId = player.getUniqueId();

                if (toggledPlayers.contains(playerId)) {
                    toggledPlayers.remove(playerId);
                    player.sendMessage(prefix + "Toggled the action bar clock off");
                } else {
                    toggledPlayers.add(playerId);
                    player.sendMessage(prefix + "Toggled the action bar clock on");
                }
            } else {
                player.sendMessage(prefix + "Too many arguments.");
            }
            return true;
        }
        return false;
    }

}

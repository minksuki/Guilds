/*
 * MIT License
 *
 * Copyright (c) 2018 Glare
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package me.glaremasters.guilds.listeners;

import ch.jalu.configme.SettingsManager;
import co.aikar.commands.ACFBukkitUtil;
import co.aikar.commands.PaperCommandManager;
import lombok.AllArgsConstructor;
import me.glaremasters.guilds.Guilds;
import me.glaremasters.guilds.configuration.sections.GuildSettings;
import me.glaremasters.guilds.configuration.sections.PluginSettings;
import me.glaremasters.guilds.guild.Guild;
import me.glaremasters.guilds.guild.GuildHandler;
import me.glaremasters.guilds.utils.JSONMessage;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static me.glaremasters.guilds.utils.StringUtils.color;

/**
 * Created by GlareMasters
 * Date: 7/19/2018
 * Time: 5:31 PM
 */
@AllArgsConstructor
public class PlayerListener implements Listener {

    //todo

    private GuildHandler guildHandler;
    private SettingsManager settingsManager;
    private Guilds guilds;
    private PaperCommandManager commandManager;

    private final Set<UUID> ALREADY_INFORMED = new HashSet<>();

    /**
     * This will check if a user is OP and will inform them of any important announcements from the Guild's Developer
     * @param event
     */
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (settingsManager.getProperty(PluginSettings.ANNOUNCEMENTS_IN_GAME)) {
            guilds.getServer().getScheduler().scheduleAsyncDelayedTask(guilds, () -> {
                if (player.isOp()) {
                    if (!ALREADY_INFORMED.contains(player.getUniqueId())) {
                        try {
                            JSONMessage.create(color("&f[&aGuilds&f]&r Announcements (Hover over me for more information)")).tooltip(guilds.getAnnouncements()).openURL(guilds.getDescription().getWebsite()).send(player);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        ALREADY_INFORMED.add(player.getUniqueId());
                    }
                }
            }, 70L);
        }
    }

    /**
     * Handles guild chat
     * @param event
     */
    @EventHandler(priority = EventPriority.HIGH)
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        Guild guild = guildHandler.getGuild(player);

        if (guild == null) return;

        if (guildHandler.checkGuildChat(player)) {
            guild.sendMessage(ACFBukkitUtil.color(settingsManager.getProperty(GuildSettings.GUILD_CHAT_FORMAT).replace("{role}", guildHandler.getGuildRole(guild.getMember(player.getUniqueId()).getRole().getLevel()).getName()).replace("{player}", player.getName()).replace("{message}", event.getMessage())));
            guildHandler.getSpies().forEach(s -> s.sendMessage(ACFBukkitUtil.color(settingsManager.getProperty(GuildSettings.SPY_CHAT_FORMAT).replace("{role}", guildHandler.getGuildRole(guild.getMember(player.getUniqueId()).getRole().getLevel()).getName()).replace("{player}", player.getName()).replace("{message}", event.getMessage()).replace("{guild}", guild.getName()))));
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void chatLeave(PlayerQuitEvent event) {
        guildHandler.chatLogout(event.getPlayer());
    }
}

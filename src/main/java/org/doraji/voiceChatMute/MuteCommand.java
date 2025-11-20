package org.doraji.voiceChatMute;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MuteCommand implements CommandExecutor {

    private final VoiceChatMute plugin;

    public MuteCommand(VoiceChatMute plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 1) {
            sender.sendMessage("§c사용법: /mute <플레이어이름>");
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage("§c플레이어 '" + args[0] + "'을(를) 찾을 수 없습니다.");
            return true;
        }

        plugin.mutePlayer(target.getUniqueId());
        sender.sendMessage("§a" + target.getName() + "님을 보이스챗에서 음소거했습니다.");
        return true;
    }
}
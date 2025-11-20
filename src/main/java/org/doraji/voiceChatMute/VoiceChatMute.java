package org.doraji.voiceChatMute;

import de.maxhenkel.voicechat.api.BukkitVoicechatService;
import de.maxhenkel.voicechat.api.VoicechatPlugin;
import de.maxhenkel.voicechat.api.events.EventRegistration;
import de.maxhenkel.voicechat.api.events.MicrophonePacketEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public final class VoiceChatMute extends JavaPlugin implements VoicechatPlugin {

    public static final String PLUGIN_ID = "VoiceChatMute";
    private final Set<UUID> mutedPlayers = Collections.synchronizedSet(new HashSet<>());

    @Override
    public String getPluginId() {
        return PLUGIN_ID;
    }

    @Override
    public void registerEvents(EventRegistration registration) {
        registration.registerEvent(MicrophonePacketEvent.class, this::onMicrophonePacket);
    }

    @Override
    public void onEnable() {
        BukkitVoicechatService service = getServer().getServicesManager().load(BukkitVoicechatService.class);
        if (service == null) {
            getLogger().severe("Simple Voice Chat 플러그인을 찾을 수 없습니다! 플러그인을 비활성화합니다.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        service.registerPlugin(this);

        getLogger().info("Simple Voice Chat API 이벤트 리스너가 성공적으로 등록되었습니다.");

        Objects.requireNonNull(getCommand("vcmute")).setExecutor(new MuteCommand(this));
        Objects.requireNonNull(getCommand("vcunmute")).setExecutor(new UnmuteCommand(this));
        getLogger().info("VoiceChatMute 플러그인이 활성화되었습니다.");
    }

    public void onMicrophonePacket(MicrophonePacketEvent event) {
        if (event.getSenderConnection() == null || event.getSenderConnection().getPlayer() == null) {
            return;
        }
        UUID playerUuid = event.getSenderConnection().getPlayer().getUuid();

        if (isMuted(playerUuid)) {
            event.cancel();
        }
    }

    public boolean isMuted(UUID playerUuid) {
        return mutedPlayers.contains(playerUuid);
    }

    public void mutePlayer(UUID playerUuid) {
        mutedPlayers.add(playerUuid);
    }

    public void unmutePlayer(UUID playerUuid) {
        mutedPlayers.remove(playerUuid);
    }
}
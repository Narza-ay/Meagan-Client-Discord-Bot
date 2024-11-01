package fr.meagan.discord.audio;

import java.util.Map;

import org.apache.commons.collections4.map.HashedMap;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import net.dv8tion.jda.api.entities.Guild;

public class PlayerManager {
	
	private static PlayerManager instance;
	private Map<Long, GuildMusicManager> guildMusicManagers = new HashedMap<>();
	private AudioPlayerManager audioPlayerManager = new DefaultAudioPlayerManager();

	private PlayerManager() {
		AudioSourceManagers.registerRemoteSources(audioPlayerManager);
	}
	
	public static PlayerManager get() {
		if(instance == null) {
			instance = new PlayerManager();
		}
		return instance;
	}
	
	public GuildMusicManager getGuildMusicManager(Guild guild) {
		return guildMusicManagers.computeIfAbsent(guild.getIdLong(), (guildId) -> {
			GuildMusicManager musicManager = new GuildMusicManager(audioPlayerManager);
			guild.getAudioManager().setSendingHandler(musicManager.getAudioManager());
			return musicManager;
		});
	}
	
	public void play(Guild guild, String trackURL) {
		GuildMusicManager guildMusicManager = getGuildMusicManager(guild);
		audioPlayerManager.loadItemOrdered(guildMusicManager, trackURL, new AudioLoadResultHandler() {
			
			@Override
			public void trackLoaded(AudioTrack track) {
				guildMusicManager.getTrackScheduler().queue(track);
			}
			
			@Override
			public void playlistLoaded(AudioPlaylist playList) {
				
			}
			
			@Override
			public void noMatches() {
				
			}
			
			@Override
			public void loadFailed(FriendlyException exception) {
				exception.printStackTrace();
			}
		});
	}
	
}

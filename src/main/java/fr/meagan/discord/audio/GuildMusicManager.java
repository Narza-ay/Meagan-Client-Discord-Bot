package fr.meagan.discord.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;

public class GuildMusicManager {
	
	private TrackScheduler trackScheduler;
	private AudioForwarder audioForwarder;
	
	public GuildMusicManager(AudioPlayerManager manager) {
		AudioPlayer player = manager.createPlayer();
		trackScheduler = new TrackScheduler(player);
		player.addListener(trackScheduler);
		audioForwarder = new AudioForwarder(player);
		player.setVolume(30);
	}
	
	public TrackScheduler getTrackScheduler() {
		return trackScheduler;
	}
	
	public AudioForwarder getAudioManager() {
		return audioForwarder;
	}
	
}

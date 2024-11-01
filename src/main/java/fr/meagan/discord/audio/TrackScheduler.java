package fr.meagan.discord.audio;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

public class TrackScheduler extends AudioEventAdapter {
	
	private AudioPlayer player;
	private BlockingQueue<AudioTrack> queue = new LinkedBlockingQueue<>();
	
	public TrackScheduler(AudioPlayer player) {
		this.player = player;
		this.queue = new LinkedBlockingQueue<>();
	}

	public void nextTrack() {
		this.player.startTrack(this.queue.poll(), false);
	}

	public void queue(AudioTrack track) {
		if(!player.startTrack(track, true)) {
			queue.offer(track);
		}
	}

	public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
		player.startTrack(queue.poll(), false);
	}
	
	public AudioPlayer getPlayer() {
		return player;
	}
	
	public BlockingQueue<AudioTrack> getQueue() {
		return queue;
	}
	
}

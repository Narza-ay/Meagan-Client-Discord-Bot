package fr.meagan.discord.audio;

import java.nio.ByteBuffer;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.playback.MutableAudioFrame;

import net.dv8tion.jda.api.audio.AudioSendHandler;

public class AudioForwarder implements AudioSendHandler {
	
	private AudioPlayer player;
	private ByteBuffer buffer = ByteBuffer.allocate(1024);
	private MutableAudioFrame frame = new MutableAudioFrame();
	
	public AudioForwarder(AudioPlayer player) {
		this.player = player;
		frame.setBuffer(buffer);
	}
	
	@Override
	public boolean canProvide() {
		return player.provide(frame);
	}

	@Override
	public ByteBuffer provide20MsAudio() {
		return (ByteBuffer) buffer.flip();
	}
	
	@Override
	public boolean isOpus() {
		return true;
	}

}

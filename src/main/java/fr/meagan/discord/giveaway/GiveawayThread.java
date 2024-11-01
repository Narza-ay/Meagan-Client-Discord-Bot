package fr.meagan.discord.giveaway;

import java.util.List;

import fr.meagan.discord.utils.ReformatTimeUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.User;

public class GiveawayThread implements Runnable {

	public static Thread thread;
	private int time;
	private int winners;
	private Message message;
	private static GiveawayThread giveawayThread;

	public GiveawayThread(int time, int winners, Message message) {
		this.time = time;
		this.winners = winners;
		this.message = message;
	}

	public static void start(int time, int winners, Message message) {
		giveawayThread = new GiveawayThread(time, winners, message);
		thread = new Thread(giveawayThread);
		thread.start();
	}

	public static void stopThread(Boolean deleteOriginalMessage) {
		if (deleteOriginalMessage) {
			giveawayThread.message.delete().queue();
		}
		thread.stop();
	}

	@Override
	public void run() {
		while (true) {
			while (time > 5) {
				EmbedBuilder messageBuilder = new EmbedBuilder();
				messageBuilder.setColor(0x909D90);
				messageBuilder.setFooter(
						"Prend fin dans " + ReformatTimeUtil.secondsToTime(time) + "! Nombre de gagnant(s): " + winners,
						message.getEmbeds().get(0).getFooter().getIconUrl());
				messageBuilder.setTitle(message.getEmbeds().get(0).getTitle());
				messageBuilder.setDescription(message.getEmbeds().get(0).getDescription());
				message.editMessageEmbeds(messageBuilder.build()).queue();
				time -= 5;
				try {
					Thread.sleep(5000);
				} catch (Exception e) {
				}
			}
			while (time > 0) {
				EmbedBuilder messageBuilder = new EmbedBuilder();
				messageBuilder.setColor(0x909D90);
				messageBuilder.setFooter(
						"Prend fin dans " + ReformatTimeUtil.secondsToTime(time) + "! Nombre de gagnant(s): " + winners,
						message.getEmbeds().get(0).getFooter().getIconUrl());
				messageBuilder.setTitle(message.getEmbeds().get(0).getTitle());
				messageBuilder.setDescription(message.getEmbeds().get(0).getDescription());
				message.editMessageEmbeds(messageBuilder.build()).queue();
				time--;
				try {
					Thread.sleep(1000);
				} catch (Exception e) {
				}
			}

			EmbedBuilder messageBuilder = new EmbedBuilder();
			messageBuilder.setColor(0x909D90);
			messageBuilder.setFooter("Ce giveaway est terminé. Nombre de gagnant(s): " + winners,
					message.getEmbeds().get(0).getFooter().getIconUrl());
			messageBuilder.setTitle(message.getEmbeds().get(0).getTitle() + " - Giveaway terminé");

			message.getJDA().getTextChannelById(message.getChannel().getId()).retrieveMessageById(message.getId())
					.queue(message -> {
						List<MessageReaction> reactions = message.getReactions();
						messageBuilder.setDescription("Gagnant(s):");
						for (MessageReaction reaction : reactions) {
							reaction.retrieveUsers().queue(users -> {
								users.removeIf(user -> user.equals(message.getJDA().getSelfUser()));
								for (int i = 0; i < winners; i++) {
									int randomIndex = (int) (Math.random() * users.size());
									if (!users.isEmpty()) {
										User winner = users.get(randomIndex);
										messageBuilder.appendDescription("\n\u2022 <@" + winner.getId() + ">");
									} else {
										messageBuilder.appendDescription("\n\u2022 (Personne)");
									}
									users.remove(randomIndex);
								}
							});
						}
					});
			;

			try {
				Thread.sleep(500);
			} catch (Exception e) {
			}

			Giveaway.alreadyGiveaway = false;
			message.editMessageEmbeds(messageBuilder.build()).queue();
			stopThread(false);
		}
	}
}
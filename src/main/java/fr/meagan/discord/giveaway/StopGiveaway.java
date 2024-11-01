package fr.meagan.discord.giveaway;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class StopGiveaway extends ListenerAdapter {

	public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
		if (!event.getName().equals("stopgiveaway") || !event.getMember().getRoles().stream()
				.anyMatch(role -> role.getPermissions().contains(Permission.ADMINISTRATOR))) {
			return;
		}
		if (!Giveaway.alreadyGiveaway) {
			EmbedBuilder message = new EmbedBuilder();
			message.setColor(0x202020);
			message.setThumbnail(
					"https://cdn.discordapp.com/attachments/1105126927511072878/1257439877771952128/OIG3.VoQsp0EwIeOeGAVwIuN9a554.png");
			message.setFooter("Écrivez +help dans un salon pour avoir de l'aide !",
					"https://cdn.discordapp.com/attachments/1105126927511072878/1105128624731979936/721f98d2ad64bc9a005819bddc2eb322.png");
			message.setTitle("Cadeaux");
			message.setDescription("Aucun giveaway en cours.");
			event.replyEmbeds(message.build()).queue();
			return;
		}
		Giveaway.alreadyGiveaway = false;
		GiveawayThread.stopThread(true);
		EmbedBuilder message = new EmbedBuilder();
		message.setColor(0x202020);
		message.setTitle("Cadeaux");
		message.setDescription("Giveaway actuelle arrêtée.");
		event.replyEmbeds(message.build()).queue(msg -> {
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						Thread.sleep(3000);
					} catch (Exception e) {
					}
					msg.deleteOriginal().queue();
				}
			}).start();
		});
	}

}

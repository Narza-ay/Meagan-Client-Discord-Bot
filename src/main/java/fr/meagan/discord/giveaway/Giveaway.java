package fr.meagan.discord.giveaway;

import fr.meagan.discord.utils.ReformatTimeUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Giveaway extends ListenerAdapter {

	public static boolean alreadyGiveaway = false;

	public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
		if (!event.getName().equals("giveaway") || !event.getMember().getRoles().stream()
				.anyMatch(role -> role.getPermissions().contains(Permission.ADMINISTRATOR))) {
			return;
		}
		String title = event.getOption("titre").getAsString();
		String description = event.getOption("description").getAsString();
		int time = event.getOption("temps").getAsInt();
		int numberOfWinners = event.getOption("gagnants").getAsInt();
		TextChannel channel = event.getJDA().getTextChannelById("1005101289480409131");
		if (alreadyGiveaway) {
			EmbedBuilder message = new EmbedBuilder();
			message.setColor(0x202020);
			message.setThumbnail(
					"https://cdn.discordapp.com/attachments/1105126927511072878/1257439877771952128/OIG3.VoQsp0EwIeOeGAVwIuN9a554.png");
			message.setFooter("Écrivez +help dans un salon pour avoir de l'aide !",
					"https://cdn.discordapp.com/attachments/1105126927511072878/1105128624731979936/721f98d2ad64bc9a005819bddc2eb322.png");
			message.setTitle("Cadeaux");
			message.setDescription("Il y a déjà un giveaway en cours.");
			channel.sendMessageEmbeds(message.build()).queue();
			return;
		}
		alreadyGiveaway = true;
		EmbedBuilder message = new EmbedBuilder();
		message.setColor(0x202020);
		message.setFooter("Durée: " + ReformatTimeUtil.secondsToTime(time) + " ! Nombre de gagnant(s): " + numberOfWinners,
				"https://cdn.discordapp.com/attachments/1105126927511072878/1105881453427970078/b052a4bef57c1aa73cd7cff5bc4fb61d.png");
		message.setTitle(title);
		message.setDescription(description);
		channel.sendMessageEmbeds(message.build()).queue(m -> {
			m.addReaction(Emoji.fromUnicode("\uD83C\uDF89")).queue();
			GiveawayThread.start(time, numberOfWinners, m);
		});
	}

}
package fr.meagan.discord.events;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

public class Ticket extends ListenerAdapter {

	private final Map<Long, TextChannel> ticketChannels = new HashMap<>();
	private final String ticketCategoryId = "975399940492779535";
	private final String ticketManagerRoleId = "975339903611187204";
	private final String ticketMemberRoleId = "975339903611187201";

	@Override
	public void onButtonInteraction(ButtonInteractionEvent event) {
		if (event.getComponentId().equals("open")) {
			User user = event.getUser();
			Member member = event.getMember();
			TextChannel ticketChannel = event.getGuild().getCategoryById(ticketCategoryId)
					.createTextChannel("ticket-" + user.getGlobalName()).setTopic(member.getId()).complete();
			ticketChannels.put(ticketChannel.getIdLong(), ticketChannel);
			event.reply("Ticket crée: " + ticketChannel.getAsMention()).setEphemeral(true).queue();
			EmbedBuilder message = new EmbedBuilder();
			message.setColor(0xFFBF15);
			message.setTitle("Support");
			message.setDescription("Ticket ouvert par " + member.getAsMention()
					+ "\n\nMerci de patienter, un " + event.getGuild().getRoleById(ticketManagerRoleId).getAsMention() + " viendra répondre rapidement.");
			ticketChannel.getManager()
					.putPermissionOverride(event.getMember(),
							EnumSet.of(Permission.VIEW_CHANNEL, Permission.MESSAGE_SEND), null)
					.putPermissionOverride(event.getGuild().getRoleById(ticketMemberRoleId), null,
							EnumSet.of(Permission.VIEW_CHANNEL, Permission.MESSAGE_SEND))
					.putPermissionOverride(event.getGuild().getRoleById(ticketManagerRoleId),
							EnumSet.of(Permission.VIEW_CHANNEL, Permission.MESSAGE_SEND), null)
					.queue();
			ticketChannel.sendMessageEmbeds(message.build())
					.addActionRow(Button.secondary("close", "Fermer").withEmoji(Emoji.fromUnicode("\u274C"))).queue();
		} else if (event.getComponentId().equals("close")) {
			event.getChannel().delete().queue();
		}
	}

}

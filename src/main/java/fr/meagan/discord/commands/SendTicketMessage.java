package fr.meagan.discord.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.util.concurrent.TimeUnit;

public class SendTicketMessage extends ListenerAdapter {

    private final String ticketChannelId = "975399317269516338";

    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (!event.getName().equals("send-ticket-message") || !event.getMember().getRoles().stream()
                .anyMatch(role -> role.getPermissions().contains(Permission.ADMINISTRATOR))) {
            return;
        }
        TextChannel channel = event.getJDA().getTextChannelById(ticketChannelId);
        EmbedBuilder message = new EmbedBuilder();
        message.setColor(0xF8312F);
        message.setFooter("Si tu as des problèmes, contacte nous dans le salon support !",
                "https://cdn.discordapp.com/attachments/1105126927511072878/1105128624731979936/721f98d2ad64bc9a005819bddc2eb322.png");
        message.setTitle("Support");
        message.setDescription(
                "Pour avoir de l'aide cliquez sur le bouton ci-dessous !\nUn ticket sera alors automatiquement crée et vous pourrez obtenir de l'aide !");
        channel.sendMessageEmbeds(message.build())
                .addActionRow(Button.secondary("open", "Créer un ticket").withEmoji(Emoji.fromUnicode("\uD83E\uDD1D")))
                .queue();
        event.reply("Message envoyé.").setEphemeral(true).queue(replyHook ->
                replyHook.deleteOriginal().queueAfter(5, TimeUnit.SECONDS)
        );
    }

}

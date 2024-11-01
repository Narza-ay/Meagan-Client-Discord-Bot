package fr.meagan.discord.events;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class GuildMemberJoin extends ListenerAdapter {

    private final String welcomeChannelId = "975376749938503680";

    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        TextChannel channel = event.getJDA().getTextChannelById(welcomeChannelId);
        if (channel != null) {
            EmbedBuilder message = new EmbedBuilder();
            message.setColor(0xFFBF15);
            message.setThumbnail(event.getGuild().getIconUrl());
            message.setFooter("Si tu as des problèmes, contacte nous dans le salon support !",
                    "https://cdn.discordapp.com/attachments/1105126927511072878/1105128624731979936/721f98d2ad64bc9a005819bddc2eb322.png");
            message.setTitle("Bienvenue");
            message.setDescription("Salut <@" + event.getMember().getId()
                    + ">, bienvenue sur **Meagan Client**!\n\nN'oublie pas d'aller voir les règles ! N'oublie pas de compléter les questions de personnalisation dans le salon \"Salons et rôles\" en haut du menu !");
            channel.sendMessageEmbeds(message.build()).queue();
        }
    }
}

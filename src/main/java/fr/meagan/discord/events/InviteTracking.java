package fr.meagan.discord.events;

import net.dv8tion.jda.api.entities.Invite;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.*;

public class InviteTracking extends ListenerAdapter {

    private final Map<String, String> inviteMap = new HashMap<>();  // Map des codes d'invitations
    private final Map<String, Integer> inviteCount = new HashMap<>(); // Map des invitations réussies
    private final Map<String, List<String>> inviteesList = new HashMap<>(); // Liste des membres invités par utilisateur

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        Guild guild = event.getGuild();
        User newUser = event.getUser();

        guild.retrieveInvites().queue(invites -> {
            for (Invite invite : invites) {
                if (invite.getUses() > Integer.parseInt(inviteMap.getOrDefault(invite.getCode(), "0"))) {
                    // Le code d'invitation utilisé
                    String inviterId = invite.getInviter().getId();
                    inviteMap.put(invite.getCode(), String.valueOf(invite.getUses()));

                    // Incrémente le compteur d'invites de l'invitant
                    inviteCount.put(inviterId, inviteCount.getOrDefault(inviterId, 0) + 1);

                    // Ajoute le nouvel utilisateur à la liste des invités par cet utilisateur
                    inviteesList.computeIfAbsent(inviterId, k -> new ArrayList<>()).add(newUser.getAsTag());
                    break;
                }
            }
        });
    }

    @Override
    public void onGuildMemberRemove(GuildMemberRemoveEvent event) {
        String userId = event.getUser().getId();

        inviteesList.forEach((inviterId, invitees) -> {
            if (invitees.removeIf(userTag -> userTag.equals(event.getUser().getAsTag()))) {
                inviteCount.put(inviterId, inviteCount.getOrDefault(inviterId, 1) - 1);
            }
        });
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getName().equals("invites")) {
            User targetUser = event.getOption("utilisateur").getAsUser();
            String targetUserId = targetUser.getId();

            int count = inviteCount.getOrDefault(targetUserId, 0);
            List<String> invitees = inviteesList.getOrDefault(targetUserId, Collections.emptyList());

            String inviteesString = String.join(", ", invitees);
            String response = String.format("**%s** a invité %d membres : %s", targetUser.getAsTag(), count, inviteesString);

            event.reply(response).queue();
        }
    }

}
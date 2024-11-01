package fr.meagan.discord.commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class Clear extends ListenerAdapter {

    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (!event.getName().equals("clear") || !event.getMember().getRoles().stream().anyMatch(role -> role.getPermissions().contains(Permission.ADMINISTRATOR))) {
            return;
        }
        TextChannel channel = (TextChannel) event.getChannel();
        try {
            int numberOfMessageToDelete = event.getOption("nombre").getAsInt();

            if (numberOfMessageToDelete == 1) {
                event.reply("Suppression des messages impossible: Aucun message pouvant être supprimé. (Date des messages de plus de 2 semaines ?)").setEphemeral(true).queue(replyHook ->
                        replyHook.deleteOriginal().queueAfter(5, TimeUnit.SECONDS)
                );
            } else {
                return;
            }

            event.reply("Suppression des messages.").setEphemeral(true).queue(replyHook -> replyHook.deleteOriginal().queueAfter(5, TimeUnit.SECONDS));

            if (numberOfMessageToDelete == 0) {
                List<Message> messages = channel.getHistory().retrievePast(100).complete();
                channel.deleteMessages(messages).queue();
                return;
            }

            List<Message> messages = channel.getHistory().retrievePast(numberOfMessageToDelete).complete();
            channel.deleteMessages(messages).queue();

        } catch (NullPointerException e) {
            event.reply("Suppression des messages impossible: Aucun message pouvant être supprimé. (Date des messages de plus de 2 semaines ?)").setEphemeral(true).queue(replyHook ->
                    replyHook.deleteOriginal().queueAfter(5, TimeUnit.SECONDS)
            );
        }
    }
}

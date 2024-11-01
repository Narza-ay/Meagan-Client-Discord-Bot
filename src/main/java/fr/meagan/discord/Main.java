package fr.meagan.discord;

import fr.meagan.discord.commands.Clear;
import fr.meagan.discord.commands.SendTicketMessage;
import fr.meagan.discord.events.GuildMemberJoin;
import fr.meagan.discord.events.InviteTracking;
import fr.meagan.discord.events.Ticket;
import fr.meagan.discord.giveaway.Giveaway;
import fr.meagan.discord.giveaway.StopGiveaway;
import fr.meagan.discord.audio.PlayerManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.managers.AudioManager;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.jetbrains.annotations.NotNull;

public class Main extends ListenerAdapter {

    public static JDA jda;
    public static String token = "YOUR_TOKEN";
    public static OnlineStatus status = OnlineStatus.ONLINE;
    public static Activity activity = Activity.playing("Meagan Launcher");

    private static final String guildId = "975339903611187200";

    private static final String membersChannelId = "975411354699718716";
    private static final String totalChannelId = "975411350773837938";
    private static final String boostsChannelId = "1229857654759886849";

    private static final String boostRoleId = "1117170410564366397";

    private static final String musicVoiceChannelId = "1300206532776169493";
    private static final String radioStreamUrl = "https://live.hunter.fm/lofi_high";

    public static void main(String[] args) throws InterruptedException {
        jda = JDABuilder.createDefault(token).enableIntents(GatewayIntent.GUILD_PRESENCES, GatewayIntent.GUILD_MEMBERS, GatewayIntent.MESSAGE_CONTENT).enableCache(CacheFlag.VOICE_STATE).setStatus(status).setActivity(activity).build();
        jda.addEventListener(new Main());
        jda.addEventListener(new GuildMemberJoin());
        jda.addEventListener(new SendTicketMessage());
        jda.addEventListener(new Ticket());
        jda.addEventListener(new Giveaway());
        jda.addEventListener(new StopGiveaway());
        jda.addEventListener(new InviteTracking());
        jda.addEventListener(new Clear());
        jda.awaitReady();
    }

    public void onReady(@NotNull ReadyEvent event) {
        jda.updateCommands()
                .addCommands(
                        Commands.slash("send-ticket-message", "Envoyer le message pour les Tickets"),
                        Commands.slash("invites", "Affiche les invitations d'un utilisateur")
                                .addOption(net.dv8tion.jda.api.interactions.commands.OptionType.USER, "utilisateur", "L'utilisateur en question", true),
                        Commands.slash("giveaway", "Faire un giveaway")
                                .addOption(OptionType.STRING, "titre", "titre", true)
                                .addOption(OptionType.STRING, "description", "description")
                                .addOption(OptionType.INTEGER, "temps", "temps")
                                .addOption(OptionType.INTEGER, "gagnants", "nombres de gagnants"),
                        Commands.slash("stopgiveaway", "Arrete un giveaway"),
                        Commands.slash("clear", "Supprime tous les messages.")
                                .addOption(OptionType.INTEGER, "nombre", "nombre de messages à supprimer"))
                .queue();

        new Thread(new update()).start();

        VoiceChannel voiceChannel = event.getJDA().getVoiceChannelById(musicVoiceChannelId);
        AudioManager audioManager = voiceChannel.getGuild().getAudioManager();
        audioManager.openAudioConnection(voiceChannel);
        PlayerManager playerManager = PlayerManager.get();
        playerManager.play(Main.jda.getGuildById(guildId), radioStreamUrl);
    }

    public static class update implements Runnable {
        private int botCount = 0;
        private int memberCount = 0;
        private int boostCount = 0;

        VoiceChannel members = jda.getVoiceChannelById(membersChannelId);
        VoiceChannel total = jda.getVoiceChannelById(totalChannelId);
        VoiceChannel boosts = jda.getVoiceChannelById(boostsChannelId);

        @Override
        public void run() {
            while (true) {
                jda.getGuildById(guildId).loadMembers().onSuccess(mbs -> {

                    for (Member member : mbs) {
                        if (member.getUser().isBot()) {
                            botCount++;
                        } else {
                            memberCount++;
                        }
                        if (member.getRoles()
                                .contains(jda.getGuildById(guildId).getRoleById(boostRoleId))) {
                            boostCount++;
                        }
                    }
                    updateVoiceChannelWithNumber(members, memberCount);
                    updateVoiceChannelWithNumber(total, memberCount + botCount);
                    updateVoiceChannelWithNumber(boosts, boostCount);
                });
                try {
                    Thread.sleep(120000);
                } catch (Exception e) {
                }
            }
        }

        public void updateVoiceChannelWithNumber(VoiceChannel channel, int number) {
            channel.getManager().setName(channel.getName().replaceAll("[0-9]", "") + number).queue();
        }
    }
}
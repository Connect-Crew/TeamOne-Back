package com.connectcrew.teamone.userservice.notification.adapter.out.discord;

import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class DiscordNotificationAdapter {

    private final String token;

    private final JDA jda;

    private final TextChannel reportChannel;
    private final TextChannel errorChannel;

    public DiscordNotificationAdapter(@Value("${discord.bot.token}") String token, @Value("${discord.channel.report}") String reportChannel, @Value("${discord.channel.error}") String errorChannel) {
        this.token = token;
        try {
            jda = JDABuilder.createDefault(token)
                    .setActivity(Activity.playing("모니터링"))
                    .build().awaitReady();

            this.reportChannel = jda.getTextChannelById(reportChannel);
            this.errorChannel = jda.getTextChannelById(errorChannel);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

//    public void sendReport(String message) {
//        reportChannel.sendMessage(message).queue();
//    }
}

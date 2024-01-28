package com.connectcrew.teamone.userservice.notification.adapter.out.discord;

import com.connectcrew.teamone.userservice.notification.application.port.out.SendDiscordOutput;
import com.connectcrew.teamone.userservice.notification.domain.DiscordChannel;
import com.connectcrew.teamone.userservice.notification.domain.DiscordMessage;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Objects;

@Slf4j
@Repository
public class DiscordNotificationAdapter implements SendDiscordOutput {

    private final String token;

    private final JDA jda;

    private final Map<DiscordChannel, TextChannel> channels;

    public DiscordNotificationAdapter(
            @Value("${discord.bot.token}") String token,
            @Value("${discord.channel.report}") String reportChannel,
            @Value("${discord.channel.error}") String errorChannel,
            @Value("${discord.channel.wish}") String wishChannel
    ) {
        this.token = token;
        try {
            jda = JDABuilder.createDefault(token)
                    .setActivity(Activity.playing("모니터링"))
                    .build().awaitReady();

            channels = Map.of(
                    DiscordChannel.REPORT, Objects.requireNonNull(jda.getTextChannelById(reportChannel)),
                    DiscordChannel.ERROR, Objects.requireNonNull(jda.getTextChannelById(errorChannel)),
                    DiscordChannel.WISH, Objects.requireNonNull(jda.getTextChannelById(wishChannel))
            );
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public boolean sendMessage(DiscordMessage message) {
        log.trace("send message: {}", message);
        try {
            channels.get(message.channel()).sendMessage(message.toMessage()).queue();
            return true;
        } catch (Exception e) {
            log.trace("send message - error: {}", e.getMessage(), e);
            return false;
        }
    }
}

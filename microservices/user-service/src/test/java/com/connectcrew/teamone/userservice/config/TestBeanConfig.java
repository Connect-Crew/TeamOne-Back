package com.connectcrew.teamone.userservice.config;

import com.connectcrew.teamone.userservice.favorite.adapter.out.persistence.repository.FavoriteRepository;
import com.connectcrew.teamone.userservice.notification.adapter.out.discord.DiscordNotificationAdapter;
import com.connectcrew.teamone.userservice.notification.adapter.out.persistence.repository.FcmRepository;
import com.connectcrew.teamone.userservice.profile.adapter.out.persistence.repository.PartRepository;
import com.connectcrew.teamone.userservice.profile.adapter.out.persistence.repository.ProfileRepository;
import com.connectcrew.teamone.userservice.profile.adapter.out.persistence.repository.RepresentProjectRepository;
import com.connectcrew.teamone.userservice.user.adapter.out.persistence.repository.UserRepository;
import com.google.firebase.messaging.FirebaseMessaging;
import net.dv8tion.jda.api.JDA;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.MessageListenerContainer;

@TestConfiguration
public class TestBeanConfig {
    // kafka beans
    @MockBean
    KafkaTemplate<String, String> kafkaTemplate;

    @MockBean
    ProducerFactory<String, String> producerFactory;

    @MockBean
    ConsumerFactory<String, String> consumerFactory;

    @MockBean
    ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory;

    @MockBean
    MessageListenerContainer messageListenerContainer;

    @MockBean
    KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private ProfileRepository profileRepository;

    @MockBean
    private FavoriteRepository favoriteRepository;

    @MockBean
    private PartRepository partRepository;

    @MockBean
    private RepresentProjectRepository representProjectRepository;

    @MockBean
    private FcmRepository fcmRepository;

    @MockBean
    private FirebaseMessaging firebaseMessaging;

    @MockBean
    private JDA jda;

    @MockBean
    private DiscordNotificationAdapter discordNotificationAdapter;
}

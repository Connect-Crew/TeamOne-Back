package com.connectcrew.teamone.projectservice.config;

import com.connectcrew.teamone.projectservice.member.adapter.out.persistence.repository.ApplyRepository;
import com.connectcrew.teamone.projectservice.member.adapter.out.persistence.repository.MemberPartRepository;
import com.connectcrew.teamone.projectservice.member.adapter.out.persistence.repository.MemberRepository;
import com.connectcrew.teamone.projectservice.project.adapter.out.persistence.repository.*;
import io.r2dbc.spi.ConnectionFactory;
import org.springframework.boot.autoconfigure.data.r2dbc.R2dbcDataAutoConfiguration;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.r2dbc.convert.R2dbcCustomConversions;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.r2dbc.core.DatabaseClient;

@TestConfiguration
public class TestConfig {
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
    ConnectionFactory connectionFactory;

    @MockBean
    R2dbcEntityTemplate r2dbcEntityTemplate;

    @MockBean
    DatabaseClient r2dbcDatabaseClient;

    @MockBean
    R2dbcDataAutoConfiguration r2dbcDataAutoConfiguration;

    @MockBean
    R2dbcCustomConversions r2dbcCustomConversions;

    @MockBean
    ProjectRepository projectRepository;

    @MockBean
    BannerRepository bannerRepository;

    @MockBean
    CategoryRepository categoryRepository;

    @MockBean
    PartRepository partRepository;

    @MockBean
    ReportRepository reportRepository;

    @MockBean
    SkillRepository skillRepository;

    @MockBean
    ApplyRepository applyRepository;

    @MockBean
    MemberRepository memberRepository;

    @MockBean
    MemberPartRepository memberPartRepository;
}

package com.geekstack.cards.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String LIKE_QUEUE_NAME = "likeQueue";
    public static final String LIKE_EXCHANGE_NAME = "likeExchange";
    public static final String LIKE_ROUTING_KEY = "likeRoutingKey";
    public static final String UNLIKE_QUEUE_NAME = "unlikeQueue";
    public static final String UNLIKE_EXCHANGE_NAME = "unlikeExchange";
    public static final String UNLIKE_ROUTING_KEY = "unlikeRoutingKey";
    public static final String NOTIFICATIONS_QUEUE_NAME = "notificationsQueue";
    public static final String NOTIFICATIONS_EXCHANGE_NAME="notificationsExchange";
    public static final String NOTIFICATIONS_ROUTING_KEY = "notificationsRoutingKey";

    @Bean
    public Queue likesQueue() {
        return new Queue(LIKE_QUEUE_NAME, true);
    }

    @Bean
    public Queue unlikesQueue() {
        return new Queue(UNLIKE_QUEUE_NAME, true);
    }

    @Bean
    public Queue notificationsQueue(){
        return new Queue(NOTIFICATIONS_QUEUE_NAME, true);
    }

    @Bean
    public DirectExchange likesExchange() {
        return new DirectExchange(LIKE_EXCHANGE_NAME);
    }

    @Bean
    public DirectExchange unlikesExchange() {
        return new DirectExchange(UNLIKE_EXCHANGE_NAME);
    }

    @Bean
    public DirectExchange notificationsExchange(){
        return new DirectExchange(NOTIFICATIONS_EXCHANGE_NAME);
    }

    @Bean
    public Binding bindingLikesQueue() {
        return BindingBuilder.bind(likesQueue()).to(likesExchange()).with(LIKE_ROUTING_KEY);
    }

    @Bean
    public Binding bindingUnlikesQueue() {
        return BindingBuilder.bind(unlikesQueue()).to(unlikesExchange()).with(UNLIKE_ROUTING_KEY);
    }

    @Bean
    public Binding bindingNotificationsQueue(){
        return BindingBuilder.bind(notificationsQueue()).to(notificationsExchange()).with(NOTIFICATIONS_ROUTING_KEY);
    }
}

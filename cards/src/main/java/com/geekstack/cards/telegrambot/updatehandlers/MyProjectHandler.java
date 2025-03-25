/**
package com.geekstack.cards.telegrambot.updatehandlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import jakarta.annotation.PostConstruct;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class MyProjectHandler extends TelegramLongPollingBot {
    private static final Logger logger = LoggerFactory.getLogger(MyProjectHandler.class);
    
    // Interface for command handlers
    private interface CommandHandler {
        void handle(String chatId, String[] args);
    }
    
    // Map to store command handlers
    private final Map<String, CommandHandler> commandHandlers = new HashMap<>();
    
    @Value("${bot.username}")
    private String botUsername;
    
    @Value("${bot.token}")
    private String botToken;
    
    @SuppressWarnings("deprecation")
    public MyProjectHandler() {
        super();
        setupCommandHandlers();
    }
    
    @SuppressWarnings("deprecation")
    public MyProjectHandler(DefaultBotOptions options) {
        super(options);
        setupCommandHandlers();
    }
    
    @Override
    public String getBotUsername() {
        return botUsername;
    }
    
    @Override
    public String getBotToken() {
        return botToken;
    }
    
    // Initialize command handlers
    private void setupCommandHandlers() {
        // Start command handler
        commandHandlers.put("/start", (chatId, args) -> {
            SendMessage message = new SendMessage();
            message.setChatId(chatId);
            message.setText("Welcome to the bot! Use /help to see available commands.");
            sendMessage(message);
        });
        
        // Help command handler
        commandHandlers.put("/help", (chatId, args) -> {
            SendMessage message = new SendMessage();
            message.setChatId(chatId);
            message.setText("Available commands:\n" +
                    "/start - Start the bot\n" +
                    "/help - Show this help message\n" +
                    "/about - Information about this bot\n" +
                    "/echo [text] - Echo back the text you provide");
            sendMessage(message);
        });
        
        // About command handler
        commandHandlers.put("/about", (chatId, args) -> {
            SendMessage message = new SendMessage();
            message.setChatId(chatId);
            message.setText("This is a simple Telegram bot built with Java and Spring Boot.");
            sendMessage(message);
        });
        
        // Echo command handler
        commandHandlers.put("/echo", (chatId, args) -> {
            SendMessage message = new SendMessage();
            message.setChatId(chatId);
            if (args.length > 0) {
                message.setText("Echo: " + String.join(" ", args));
            } else {
                message.setText("Please provide text to echo. Usage: /echo [text]");
            }
            sendMessage(message);
        });
    }
    
    // Register commands with Telegram
    @PostConstruct
    public void registerCommands() {
        try {
            List<BotCommand> commands = new ArrayList<>();
            commands.add(new BotCommand("/start", "Start the bot"));
            commands.add(new BotCommand("/help", "Show available commands"));
            commands.add(new BotCommand("/about", "About this bot"));
            commands.add(new BotCommand("/echo", "Echo a message back"));
            
            execute(new SetMyCommands(commands, new BotCommandScopeDefault(), null));
            logger.info("Bot commands registered successfully");
        } catch (TelegramApiException e) {
            logger.error("Failed to register bot commands", e);
        }
    }
    
    // Helper method to send messages and handle exceptions
    private void sendMessage(SendMessage message) {
        try {
            execute(message);
            logger.info("Sent message to {}: {}", message.getChatId(), message.getText());
        } catch (TelegramApiException e) {
            logger.error("Failed to send message to {}", message.getChatId(), e);
        }
    }
    
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String chatId = update.getMessage().getChatId().toString();
            String text = update.getMessage().getText();
            
            // Check if message is a command
            if (text.startsWith("/")) {
                // Split the command into command name and arguments
                String[] parts = text.split("\\s+", 2);
                String command = parts[0].toLowerCase();
                String[] args = parts.length > 1 ? parts[1].split("\\s+") : new String[0];
                
                // Execute command if handler exists
                CommandHandler handler = commandHandlers.get(command);
                if (handler != null) {
                    handler.handle(chatId, args);
                    return;
                } else {
                    // Unknown command
                    SendMessage response = new SendMessage();
                    response.setChatId(chatId);
                    response.setText("Unknown command. Type /help to see available commands.");
                    sendMessage(response);
                    return;
                }
            }
            
            // Handle regular messages
            SendMessage response = new SendMessage();
            response.setChatId(chatId);
            response.setText("You said: " + text);
            sendMessage(response);
        }
    }
}
*/
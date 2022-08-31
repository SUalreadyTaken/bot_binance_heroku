package su.binance_bot;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.BotSession;

import su.binance_bot.Model.AllSleep;
import su.binance_bot.Model.DBCommandsQueue;
import su.binance_bot.Model.MessageToSend;
import su.binance_bot.Runnable.DBController;
import su.binance_bot.Runnable.ExecuteMessages;
import su.binance_bot.Service.ChatIDService;
import su.binance_bot.Service.CoinPositionService;
import su.binance_bot.Service.IdleService;

@Component
public class Init {

  @Value("${switchapp}")
  private boolean switchApp;

  private final TelegramBot telegramBot;
  private final IdleService idleService;
  private final CoinPositionService coinPositionService;
  private final ChatIDService chatIDService;
  private final DBCommandsQueue dbCommandsQueue;
  private final MessageToSend messageToSend;
  private final AllSleep allSleep;
  private BotSession botSession;
  private boolean running = true;
  private final TelegramBotsApi botsApi;

  @Autowired
  public Init(TelegramBot telegramBot, IdleService idleService, MessageToSend messageToSend,
      DBCommandsQueue dbCommandsQueue, CoinPositionService coinPositionService, ChatIDService chatIDService,
      AllSleep allSleep) {
    this.telegramBot = telegramBot;
    this.idleService = idleService;
    this.messageToSend = messageToSend;
    this.dbCommandsQueue = dbCommandsQueue;
    this.coinPositionService = coinPositionService;
    this.chatIDService = chatIDService;
    this.allSleep = allSleep;
    this.botsApi = new TelegramBotsApi();
  }

  @PostConstruct
  private void start() {
    if (!switchApp || !idleService.getAlternativeBoolean()) {
      // register telegram bot
      try {
        this.botSession = botsApi.registerBot(telegramBot);
        System.out.println("Register bot success !\nusername >> " + telegramBot.getBotUsername());
      } catch (TelegramApiException e) {
        e.printStackTrace();
      }
    }
  }

  @EventListener(ApplicationReadyEvent.class)
  public void init() {
    new Thread(new DBController(dbCommandsQueue.getDbCommandsQueue(), coinPositionService, chatIDService)).start();
    new Thread(new ExecuteMessages(messageToSend, telegramBot, allSleep)).start();
  }

  void registerOrStop() {
    if (running) {
      System.out.println("bot is running going to stop it");
      botSession.stop();
    } else {
      System.out.println("bot is stopped will start it again");
      botSession.start();
    }
    running = !running;
  }
}
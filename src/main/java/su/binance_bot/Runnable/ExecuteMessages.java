package su.binance_bot.Runnable;

import java.util.concurrent.TimeUnit;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import su.binance_bot.TelegramBot;
import su.binance_bot.Model.AllSleep;
import su.binance_bot.Model.Message;
import su.binance_bot.Model.MessageToSend;

public class ExecuteMessages implements Runnable {

  private final MessageToSend messageToSend;
  private final TelegramBot telegramBot;
  private final AllSleep allSleep;
  private int MESSAGES_SENT = 0;
  private long LAST_MESSAGE_SENT = System.currentTimeMillis();
  // hack need to fix it later.. remove chat ids that have blocked the bot and not unregistered
  private long blockedChatId;

  public ExecuteMessages(MessageToSend messageToSend, TelegramBot telegramBot, AllSleep allSleep) {
    this.messageToSend = messageToSend;
    this.telegramBot = telegramBot;
    this.allSleep = allSleep;
  }

  @Override
  public void run() {
    while (true) {
      try {
        Message message = messageToSend.getMessageQueue().take();
        blockedChatId = message.getChatId();
        while (allSleep.getIsSleep()) {
          System.out.println("ExMsg needs to sleep");
          TimeUnit.MILLISECONDS.sleep(1000);
        }
        if (message.getText().length() > 4095) {
          String tmpMsg = message.getText();
          System.out.println("Msg too long will split it");
          for(int i = 0; i < tmpMsg.length(); i+= 4095) {
            int end = tmpMsg.length() - i - 4095 > 0 ? i + 4095 : tmpMsg.length();
            String tmp = tmpMsg.substring(i, end);
            SendMessage sendMessage = new SendMessage(message.getChatId(), tmp);
            MESSAGES_SENT++;
            telegramBot.execute(sendMessage);
          }
          MESSAGES_SENT--;
        } else {
          SendMessage sendMessage = new SendMessage(message.getChatId(), message.getText());
          telegramBot.execute(sendMessage);
        }
        sleepIfNeeded();
      } catch (InterruptedException e) {
        System.out.println("ExecuteMessage queue error");
        e.printStackTrace();
      } catch (TelegramApiException e) {
        // TODO check if messange contains 'Error sending message: [403] Forbidden: bot was blocked by the user'
        // remove the user_id .. he/she forgot to /unregister and is still getting the signals :) but has blocked the bot
        // need private final ChatList chatList and ChatIdRepository to remove the chatID
        System.out.println("ExecuteMessage sendMessage error");
        System.out.println("id > " + blockedChatId);
        System.out.println();
        e.printStackTrace();
      }
      LAST_MESSAGE_SENT = System.currentTimeMillis();
    }
  }

  public void sleepIfNeeded() {
    MESSAGES_SENT++;
    if (MESSAGES_SENT >= 30) {
      try {
        TimeUnit.MILLISECONDS.sleep(1000);
        MESSAGES_SENT = 0;
      } catch (InterruptedException e) {
        System.out.println("ExecuteMessage error is sleeping");
        e.printStackTrace();
      }
    } else if (System.currentTimeMillis() - LAST_MESSAGE_SENT >= 1000) {
      MESSAGES_SENT = 1;
    }
  }

}
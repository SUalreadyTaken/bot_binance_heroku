package su.binance_bot;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import java.util.stream.Stream;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import su.binance_bot.Enum.CoinSymbolEnum;
import su.binance_bot.Enum.DBCommandType;
import su.binance_bot.Enum.PositionEnum;
import su.binance_bot.Model.ChatID;
import su.binance_bot.Model.Coin;
import su.binance_bot.Model.CoinPosition;
import su.binance_bot.Model.DBCommand;
import su.binance_bot.Model.DBCommandsQueue;
import su.binance_bot.Model.Message;
import su.binance_bot.Model.MessageToSend;
import su.binance_bot.Runnable.BinanceChecker;
import su.binance_bot.Service.CoinPositionService;

@Component
public class TelegramBot extends TelegramLongPollingBot {
  @Value("${telegram.token}")
  private String token;

  @Value("${telegram.username}")
  private String username;

  @Value("${registation.key}")
  private String registationKey;

  private final List<Coin> coinList;
  private final ChatList chatList;
  private final MessageToSend messageToSend;
  private final DBCommandsQueue dbCommandsQueue;
  private final CoinPositionService coinPositionService;

  @Autowired
  public TelegramBot(BinanceChecker binanceChecker, ChatList chatList, MessageToSend messageToSend,
      DBCommandsQueue dbCommandsQueue, CoinPositionService coinPositionService) {
    this.coinPositionService = coinPositionService;
    this.coinList = binanceChecker.getCoinList();
    this.chatList = chatList;
    this.messageToSend = messageToSend;
    this.dbCommandsQueue = dbCommandsQueue;
  }

  @Override
  public void onUpdateReceived(Update update) {
    if (update.hasMessage() && update.getMessage().hasText()) {

      String command = update.getMessage().getText();
      int chatId = Math.toIntExact(update.getMessage().getChatId());
      StringBuilder respondMessage = new StringBuilder();

      System.out.println("Got this msg >> " + update.getMessage().getText() + " | from > " + chatId);

      String[] symbols = command.trim().replaceAll("\\s+", " ").split(" ");
      if (symbols[0].equalsIgnoreCase("/unregister")) {
        this.unRegister(respondMessage, chatId);
      } else if (symbols[0].equalsIgnoreCase("/register")) {
        // no registation key needed atm
        this.registerNoKey(respondMessage, chatId);
      } else if (symbols[0].equalsIgnoreCase("/start")) {
        this.buildStartMessage(respondMessage);
      } else {
        List<String> wantedSymbols = Stream.of(symbols).skip(1).collect(Collectors.toList());
        if (!wantedSymbols.isEmpty()) {
          String commandString = symbols[0];
          switch (commandString) {
            case "/status":
              this.getStatus(respondMessage, wantedSymbols);
              break;
            case "/history":
              this.history(respondMessage, chatId, wantedSymbols);
              break;
          }
        }
      }

      if (!respondMessage.toString().isEmpty()) {
        try {
          messageToSend.getMessageQueue().put(new Message(chatId, respondMessage.toString()));
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      } else {
        System.out.println("Respond message is empty");
      }
    }

  }

  private void buildStartMessage(StringBuilder respondMessage) {
    String welcomingMessage = "⚠️Outdated bot search for binance_v92_bot for better signals.⚠️\n\n\nWelcome!\nThis is a crypto long bot for btc, eth, ltc, link and eos."
        + " Might add more coins in the future.\n" + "Altcoins are volatile, be warned.\n"
        + "To start getting the signals use /register and if you have had enough of this bot use /unregister. Just deleting the chat wont unsubscribe you from the bot.\n"
        + "To check the history of a coin use /history btc 10. This will print 10 past closed trades of btc.\n"
        + "To check the coins current status type /status btc or /status all to get every coins status";
    respondMessage.append(welcomingMessage);

  }

  private void history(StringBuilder respondMessage, int requestChatsId, List<String> command) {
    if (command.size() <= 2) {
      // TODO get all
      CoinSymbolEnum symbol = findSymbol(command.get(0));
      if (symbol != null) {
        if (command.size() == 2) {
          if (isNumber(command.get(1), respondMessage)) {
            int lookback = 2 * Integer.parseInt(command.get(1));
            List<CoinPosition> positions = coinPositionService.getCoinHistory(symbol, lookback);
            if (!positions.isEmpty() && positions.size() >= 2) {
              historyBuilder(respondMessage, symbol, positions);
            }
          }
        } else {
          List<CoinPosition> positions = coinPositionService.getCoinHistory(symbol, 10);
          historyBuilder(respondMessage, symbol, positions);
        }
      }
    }
  }

  private void historyBuilder(StringBuilder respondMessage, CoinSymbolEnum symbol, List<CoinPosition> positions) {
    Collections.reverse(positions);
    DecimalFormat df = new DecimalFormat();
    df.setMaximumFractionDigits(3);
    float profit = 1f;
    int start = 0;
    if (positions.get(start).getPosition().equals(PositionEnum.CLOSED)) {
      start = 1;
    }
    respondMessage.append(symbol.toString() + "\n");
    for (int i = start; i < positions.size() - 1; i += 2) {
      float open = positions.get(i).getPrice();
      float close = positions.get(i + 1).getPrice();
      respondMessage.append(positions.get(i).historyString() + "\n");
      respondMessage.append(positions.get(i + 1).historyString() + "\n");
      float percentage = ((close / open) - 1);
      profit = profit * (1 + percentage);
      respondMessage.append("pos = " + df.format((percentage * 100)) + "% | total = " + profit + "x\n---\n");
    }
    respondMessage.append("total profit = " + profit);
    if (positions.get(positions.size() - 1).getPosition().equals(PositionEnum.LONG)) {
      respondMessage.append("\n\nCurrent position\n" + positions.get(positions.size() - 1).historyString());
    }
  }

  private void unRegister(StringBuilder respondMessage, int requestChatsId) {
    boolean isRegistered = false;
    for (ChatID c : this.chatList.getChatIds()) {
      if (requestChatsId == c.getChatId()) {
        isRegistered = true;
        break;
      }
    }
    if (isRegistered) {
      // TODO need to change this .. should move it to chatList component and make it
      // syncronizable list .. but fk it atm
      for (int i = 0; i < this.chatList.getChatIds().size(); i++) {
        if (this.chatList.getChatIds().get(i).getChatId() == requestChatsId) {
          this.chatList.getChatIds().remove(i);
          respondMessage.append("Bye!");
          DBCommand dbCommand = new DBCommand();
          dbCommand.setDbCommandType(DBCommandType.UNREGISTER);
          dbCommand.setChatId(requestChatsId);
          try {
            this.dbCommandsQueue.getDbCommandsQueue().put(dbCommand);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
          break;
        }
      }
    }
  }

  private void registerNoKey(StringBuilder respondMessage, int requestChatsId) {
    boolean alreadyRegistered = false;
    for (ChatID c : this.chatList.getChatIds()) {
      if (requestChatsId == c.getChatId()) {
        alreadyRegistered = true;
        break;
      }
    }
    if (!alreadyRegistered) {
      this.chatList.getChatIds().add(new ChatID(requestChatsId));
      respondMessage.append("Welcome!");
      DBCommand dbCommand = new DBCommand();
      dbCommand.setDbCommandType(DBCommandType.REGISTER);
      dbCommand.setChatId(requestChatsId);
      try {
        this.dbCommandsQueue.getDbCommandsQueue().put(dbCommand);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  private void register(StringBuilder respondMessage, List<String> key, int requestChatsId) {
    String gotKey = key.get(0);
    if (gotKey.equalsIgnoreCase(this.registationKey)) {
      boolean alreadyRegistered = false;
      for (ChatID c : this.chatList.getChatIds()) {
        if (requestChatsId == c.getChatId()) {
          alreadyRegistered = true;
          break;
        }
      }
      if (!alreadyRegistered) {
        this.chatList.getChatIds().add(new ChatID(requestChatsId));
        respondMessage.append("Welcome !");
        DBCommand dbCommand = new DBCommand();
        dbCommand.setDbCommandType(DBCommandType.REGISTER);
        dbCommand.setChatId(requestChatsId);
        try {
          this.dbCommandsQueue.getDbCommandsQueue().put(dbCommand);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    }
  }

  private void getStatus(StringBuilder respondMessage, List<String> symbols) {
    if (symbols.size() == 1 && symbols.get(0).contentEquals("all")) {
      for (Coin c : this.coinList) {
        respondMessage.append(c.positionToString() + '\n');
      }
    } else {
      for (String symbol : symbols) {
        CoinSymbolEnum searching = CoinSymbolEnum.BTCUSDT;
        for (CoinSymbolEnum c : CoinSymbolEnum.values()) {
          if (c.name().contains(symbol.toUpperCase())) {
            searching = c;
            break;
          }
        }
        for (int i = 0; i < this.coinList.size(); i++) {
          if (this.coinList.get(i).getSymbol() == searching) {
            if (respondMessage.length() > 1) {
              respondMessage.append("\n" + this.coinList.get(i).positionToString());
            } else {
              respondMessage.append(this.coinList.get(i).positionToString());
            }
            break;
          }
        }
      }
    }
  }

  private boolean isNumber(String s, StringBuilder respondMessage) {
    if (NumberUtils.isParsable(s))
      return true;
    respondMessage.append("[").append(s).append("]").append(" not a number (must use . instead of , )\n");
    return false;
  }

  private CoinSymbolEnum findSymbol(String s) {
    CoinSymbolEnum res = null;
    for (int i = 0; i < CoinSymbolEnum.values().length; i++) {
      if (CoinSymbolEnum.values()[i].toString().contains(s.toUpperCase())) {
        res = CoinSymbolEnum.values()[i];
        break;
      }
    }
    return res;
  }

  @Override
  public String getBotUsername() {
    return username;
  }

  @Override
  public String getBotToken() {
    return token;
  }
}
package su.binance_bot.Runnable;

import java.util.concurrent.BlockingQueue;

import su.binance_bot.Model.DBCommand;
import su.binance_bot.Service.ChatIDService;
import su.binance_bot.Service.CoinPositionService;

public class DBController implements Runnable {

  private BlockingQueue<DBCommand> commandsQueue;
  private final CoinPositionService coinPositionService;
  private final ChatIDService chatIDService;
  

  public DBController(BlockingQueue<DBCommand> commandsQueue, CoinPositionService coinPositionService, ChatIDService chatIDService) {
    this.commandsQueue = commandsQueue;
    this.coinPositionService = coinPositionService;
    this.chatIDService = chatIDService;
  }

  @Override
  public void run() {
    while (true) {
      try {
        DBCommand dbCommand = commandsQueue.take();
        switch (dbCommand.getDbCommandType()) {
          case NEWPOSITION:
            if (dbCommand.isCoinPositionOk()) {
              this.coinPositionService.saveNewPosition(dbCommand.getCoinPosition());
            }
            break;
          case REGISTER:
            if (dbCommand.getChatId() != 0) {
              this.chatIDService.saveChat(dbCommand.getChatId());
            }
            break;
          case UNREGISTER:
            if (dbCommand.getChatId() != 0) {
              this.chatIDService.removeChat(dbCommand.getChatId());
            }
            break;
          default:
            break;
        }
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  public BlockingQueue<DBCommand> getCommandsQueue() {
    return commandsQueue;
  }

  public void setCommandsQueue(BlockingQueue<DBCommand> commandsQueue) {
    this.commandsQueue = commandsQueue;
  }
}
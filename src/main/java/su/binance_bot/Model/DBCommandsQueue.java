package su.binance_bot.Model;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.springframework.stereotype.Component;

@Component
public class DBCommandsQueue {
  private BlockingQueue<DBCommand> dbCommandsQueue = new LinkedBlockingQueue<>();

  public DBCommandsQueue() {
  }

  public BlockingQueue<DBCommand> getDbCommandsQueue() {
    return dbCommandsQueue;
  }

  public void setDbCommandsQueue(BlockingQueue<DBCommand> dbCommandsQueue) {
    this.dbCommandsQueue = dbCommandsQueue;
  }
}
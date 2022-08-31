package su.binance_bot.Model;

import su.binance_bot.Enum.DBCommandType;

public class DBCommand {
  private DBCommandType dbCommandType;
  private CoinPosition coinPosition;
  private int chatId;
 
  public DBCommand() {
  }

  public DBCommandType getDbCommandType() {
    return this.dbCommandType;
  }

  public void setDbCommandType(DBCommandType dbCommandType) {
    this.dbCommandType = dbCommandType;
  }

  public CoinPosition getCoinPosition() {
    return this.coinPosition;
  }

  public void setCoinPosition(CoinPosition coinPosition) {
    this.coinPosition = coinPosition;
  }

  public boolean isCoinPositionOk() {
    if (this.coinPosition.getDate() != null && this.coinPosition.getPosition() != null && this.coinPosition.getPrice() != 0.0f && this.coinPosition.getSymbol() != null) {
      return true;
    } else {
      return false;
    }
  }
  
  public int getChatId() {
    return this.chatId;
  }

  public void setChatId(int chatId) {
    this.chatId = chatId;
  }

}

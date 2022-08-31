package su.binance_bot.Model;

import java.time.LocalDateTime;
// import java.time.format.DateTimeFormatter;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import su.binance_bot.Enum.CoinSymbolEnum;
import su.binance_bot.Enum.PositionEnum;

@Document(collection = "coin_position")
public class CoinPosition {
  @Id
  private String id;

  private CoinSymbolEnum symbol;
  private PositionEnum position;
  private float price;
  private LocalDateTime date;
  @Transient
  private LocalDateTime warningTime;
  @Transient
  private boolean warning;

  public CoinPosition(CoinSymbolEnum symbol, PositionEnum position, float price, LocalDateTime date) {
    this.symbol = symbol;
    this.position = position;
    this.price = price;
    this.date = date;
  }


  public LocalDateTime getWarningTime() {
    return this.warningTime;
  }

  public void setWarningTime(LocalDateTime warningTime) {
    this.warningTime = warningTime;
  }

  public boolean isWarning() {
    return this.warning;
  }

  public boolean getWarning() {
    return this.warning;
  }

  public void setWarning(boolean warning) {
    this.warning = warning;
  }

  public CoinSymbolEnum getSymbol() {
    return this.symbol;
  }

  public void setSymbol(CoinSymbolEnum symbol) {
    this.symbol = symbol;
  }

  public PositionEnum getPosition() {
    return this.position;
  }

  public void setPosition(PositionEnum position) {
    this.position = position;
  }

  public float getPrice() {
    return this.price;
  }

  public void setPrice(float price) {
    this.price = price;
  }

  public LocalDateTime getDate() {
    return this.date;
  }

  public void setDate(LocalDateTime date) {
    this.date = date;
  }

  public String historyString() {
    return  getPosition() + " | price=" + getPrice() + " | date=" + getDate();
  }

  @Override
  public String toString() {
    return "{ " + getSymbol() + ", position='" + getPosition() + "'" + ", price='" + getPrice() + "'" + ", date='"
        + getDate() + "'" + " }";
  }


}

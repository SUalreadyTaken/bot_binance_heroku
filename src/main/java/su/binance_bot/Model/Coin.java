package su.binance_bot.Model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import su.binance_bot.Enum.CoinSymbolEnum;
import su.binance_bot.Enum.PositionEnum;

public class Coin {

  private CoinSymbolEnum symbol;
  private int adx;
  private int goLong;
  private int closeLong;
  private int aL;
  private int cL;
  private PositionEnum position;
  private float price;
  private LocalDateTime date;
  DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

  public Coin(CoinSymbolEnum symbol, int adx, int goLong, int closeLong, int aL, int cL, PositionEnum position,
      float price, LocalDateTime date) {
    this.symbol = symbol;
    this.adx = adx;
    this.goLong = goLong;
    this.closeLong = closeLong;
    this.aL = aL;
    this.cL = cL;
    this.position = position;
    this.price = price;
    this.date = date;
  }

  public CoinSymbolEnum getSymbol() {
    return this.symbol;
  }

  public void setSymbol(CoinSymbolEnum symbol) {
    this.symbol = symbol;
  }

  public int getAdx() {
    return this.adx;
  }

  public void setAdx(int adx) {
    this.adx = adx;
  }

  public int getGoLong() {
    return this.goLong;
  }

  public void setGoLong(int goLong) {
    this.goLong = goLong;
  }

  public int getCloseLong() {
    return this.closeLong;
  }

  public void setCloseLong(int closeLong) {
    this.closeLong = closeLong;
  }

  public int getAL() {
    return this.aL;
  }

  public void setAL(int aL) {
    this.aL = aL;
  }

  public int getCL() {
    return this.cL;
  }

  public void setCL(int cL) {
    this.cL = cL;
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

  public String positionToString() {
    return "{ " + getSymbol() + ", position=" + getPosition() + ", price=" + getPrice() + ", date="
        + getDate().format(formatter) + " }";
  }

}

package su.binance_bot.Model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import su.binance_bot.Enum.CoinSymbolEnum;


@Document(collection = "coin_config")
public class CoinConfig {
  
  @Id
  private String id;
  @Indexed(unique=true)
  private CoinSymbolEnum symbol;
  private int adx;
  private int goLong;
  private int closeLong;
  private int aL;
  private int cL;



  public CoinConfig(CoinSymbolEnum symbol, int adx, int goLong, int closeLong, int aL, int cL) {
    this.symbol = symbol;
    this.adx = adx;
    this.goLong = goLong;
    this.closeLong = closeLong;
    this.aL = aL;
    this.cL = cL;
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
  
}

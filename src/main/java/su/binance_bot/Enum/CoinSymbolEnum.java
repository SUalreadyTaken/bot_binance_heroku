package su.binance_bot.Enum;

public enum CoinSymbolEnum {
  BTCUSDT("BTCUSDT"),
  ETHUSDT("ETHUSDT"),
  LTCUSDT("LTCUSDT"),
  LINKUSDT("LINKUSDT"),
  EOSUSDT("EOSUSDT");
  
  public final String label;

    private CoinSymbolEnum(String label) {
        this.label = label;
    }
}

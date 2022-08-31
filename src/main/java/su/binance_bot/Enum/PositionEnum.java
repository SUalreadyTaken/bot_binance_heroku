package su.binance_bot.Enum;

public enum PositionEnum {
    LONG("long"),
    CLOSED("closed");
    
    public final String label;

    private PositionEnum(String label) {
        this.label = label;
    }
  
}

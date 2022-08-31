package su.binance_bot.Model;

import java.time.LocalDateTime;

/**
 * Candlestick
 */
public class MyCandlestick {
    private LocalDateTime date;
    private float open;
    private float high;
    private float low;
    private float close;
    private float volume;


    public MyCandlestick() {
    }

    public MyCandlestick(LocalDateTime date, float open, float high, float low, float close, float volume) {
        this.date = date;
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
        this.volume = volume;
    }


    public LocalDateTime getDate() {
        return this.date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public float getOpen() {
        return this.open;
    }

    public void setOpen(float open) {
        this.open = open;
    }

    public float getHigh() {
        return this.high;
    }

    public void setHigh(float high) {
        this.high = high;
    }

    public float getLow() {
        return this.low;
    }

    public void setLow(float low) {
        this.low = low;
    }

    public float getClose() {
        return this.close;
    }

    public void setClose(float close) {
        this.close = close;
    }

    public float getVolume() {
        return this.volume;
    }

    public void setVolume(float volume) {
        this.volume = volume;
    }
    

    @Override
    public String toString() {
        return "{" +
            " date='" + getDate() + "'" +
            ", open='" + getOpen() + "'" +
            ", high='" + getHigh() + "'" +
            ", low='" + getLow() + "'" +
            ", close='" + getClose() + "'" +
            ", volume='" + getVolume() + "'" +
            "}";
    }
        
}
package su.binance_bot.Utils;

import java.util.ArrayList;
import java.util.List;

import su.binance_bot.Model.Canal;
import su.binance_bot.Model.MyCandlestick;

public class CanalUtil {

  public CanalUtil() {
  }

  public Canal getCanals(List<MyCandlestick> candlestickList, int dayCount) {
    float high = 0;
    float low = 10000000;
    int highIndex = 0;
    int lowIndex = 0;
    List<Float> lowCanal = new ArrayList<>();
    List<Float> highCanal = new ArrayList<>();

    lowCanal.add(candlestickList.get(0).getClose());
    highCanal.add(candlestickList.get(0).getClose());
    for (int i = 1; i <= dayCount; i++) {
      if (candlestickList.get(i - 1).getClose() > high) {
        high = candlestickList.get(i - 1).getClose();
        highIndex = Math.abs(i - dayCount);
      }
      if (candlestickList.get(i - 1).getClose() < low) {
        low = candlestickList.get(i - 1).getClose();
        lowIndex = Math.abs(i - dayCount);
      }
      lowCanal.add(low);
      highCanal.add(high);
    }

    for (int i = dayCount + 1; i < candlestickList.size(); i++) {
      if (candlestickList.get(i - 1).getClose() <= low) {
        low = candlestickList.get(i - 1).getClose();
        lowIndex = dayCount - 1;
      } else {
        lowIndex = lowIndex - 1;
        if (lowIndex <= 0) {
          float tmpLow = 9999999;
          for (int j = i; j > i - dayCount - 1; j--) {
            if (candlestickList.get(j - 1).getClose() <= tmpLow) {
              tmpLow = candlestickList.get(j - 1).getClose();
              lowIndex = dayCount - (i - j);
            }
          }
          low = tmpLow;
        }
      }

      if (candlestickList.get(i - 1).getClose() >= high) {
        highIndex = dayCount - 1;
        high = candlestickList.get(i - 1).getClose();
      } else {
        highIndex = highIndex - 1;
        if (highIndex <= 0) {
          float tmpHigh = 0;
          for (int j = i; j > i - dayCount - 1; j--) {
            if (candlestickList.get(j - 1).getClose() >= tmpHigh) {
              tmpHigh = candlestickList.get(j - 1).getClose();
              highIndex = dayCount - (i - j);
            }
          }
          high = tmpHigh;
        }
      }
      lowCanal.add(low);
      highCanal.add(high);
    }

    Canal result = new Canal(lowCanal, highCanal);
    return result;
  }
}

package su.binance_bot.Model;

import java.util.List;

public class DI {

  List<Float> positive;
  List<Float> negative;
  List<Boolean> isPositiveOver;


  public DI(List<Float> positive, List<Float> negative, List<Boolean> isPositiveOver) {
    this.positive = positive;
    this.negative = negative;
    this.isPositiveOver = isPositiveOver;
  }

  public List<Float> getPositive() {
    return this.positive;
  }

  public void setPositive(List<Float> positive) {
    this.positive = positive;
  }

  public List<Float> getNegative() {
    return this.negative;
  }

  public void setNegative(List<Float> negative) {
    this.negative = negative;
  }

  public List<Boolean> getIsPositiveOver() {
    return this.isPositiveOver;
  }

  public void setIsPositiveOver(List<Boolean> isPositiveOver) {
    this.isPositiveOver = isPositiveOver;
  }
  

  
}
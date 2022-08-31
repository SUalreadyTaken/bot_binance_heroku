package su.binance_bot.Model;

import java.util.List;

public class Canal {
  List<Float> lowerCanalList;
  List<Float> higherCanalList;


  public Canal(List<Float> lowerCanalList, List<Float> higherCanalList) {
    this.lowerCanalList = lowerCanalList;
    this.higherCanalList = higherCanalList;
  }

  public List<Float> getLowerCanalList() {
    return this.lowerCanalList;
  }

  public void setLowerCanalList(List<Float> lowerCanalList) {
    this.lowerCanalList = lowerCanalList;
  }

  public List<Float> getHigherCanalList() {
    return this.higherCanalList;
  }

  public void setHigherCanalList(List<Float> higherCanalList) {
    this.higherCanalList = higherCanalList;
  }

}

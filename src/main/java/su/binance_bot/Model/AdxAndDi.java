package su.binance_bot.Model;

import java.util.List;

public class AdxAndDi {
  List<Float> adxList;
  DI di;

  public AdxAndDi(List<Float> adxList, DI di) {
    this.adxList = adxList;
    this.di = di;
  }

  public List<Float> getAdxList() {
    return this.adxList;
  }

  public void setAdxList(List<Float> adxList) {
    this.adxList = adxList;
  }

  public DI getDi() {
    return this.di;
  }

  public void setDi(DI di) {
    this.di = di;
  }

}

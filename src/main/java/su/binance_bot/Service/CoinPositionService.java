package su.binance_bot.Service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.PageRequest;

import su.binance_bot.Enum.CoinSymbolEnum;
import su.binance_bot.Model.CoinConfig;
import su.binance_bot.Model.CoinPosition;
import su.binance_bot.Repository.CoinPositionRepository;

@Service
@Transactional
public class CoinPositionService {

  private final CoinPositionRepository coinPositionRepository;

  public CoinPositionService(CoinPositionRepository coinPositionRepository) {
    this.coinPositionRepository = coinPositionRepository;
  }

  // Better have some coin position in db or that coin will be left behind
  public List<CoinPosition> getLatestPositions(List<CoinConfig> coinConfigList) {
    List<CoinPosition> result = new ArrayList<>();
    coinConfigList.forEach(coin -> this.coinPositionRepository.findFirstBySymbolOrderByIdDesc(coin.getSymbol())
        .ifPresent(cp -> result.add(cp)));
    return result;
  }

  public void saveNewPosition(CoinPosition coinPosition) {
    this.coinPositionRepository.insert(coinPosition);
  }

  public List<CoinPosition> getCoinHistory(CoinSymbolEnum symbol, int lookback) {
    Pageable sortedByDate = PageRequest.of(0, lookback, Sort.by("date").descending());
    List<CoinPosition> inCollection = new ArrayList<>(coinPositionRepository.findBySymbol(symbol, sortedByDate).getContent());
    return inCollection;
  }

  // TODO have to think how to get every coin x amount without multiple queries
  // public List<CoinPosition> getAllCoinHistory(List<CoinSymbolEnum> symbols, int lookback) {
  //   Pageable sortedByDate = PageRequest.of(0, lookback, Sort.by("date").descending());
  //   List<CoinPosition> inCollection = new ArrayList<>(coinPositionRepository.findBySymbolIn(symbols, sortedByDate).getContent());
  //   return inCollection;
  // }


}

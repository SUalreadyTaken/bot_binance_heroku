package su.binance_bot.Service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import su.binance_bot.Model.CoinConfig;
import su.binance_bot.Repository.CoinConfigRepository;

@Service
@Transactional
public class CoinConfigService {
  
  private final CoinConfigRepository coinCoingfigRepository;
  
	public CoinConfigService(CoinConfigRepository coinConfigRepository) {
    this.coinCoingfigRepository = coinConfigRepository;
	}
  
  public List<CoinConfig> getCoinConfigs() {
    return this.coinCoingfigRepository.findAll();
  }
}

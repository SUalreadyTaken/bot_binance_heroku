package su.binance_bot.Repository;

import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.mongodb.repository.MongoRepository;

import su.binance_bot.Enum.CoinSymbolEnum;
import su.binance_bot.Model.CoinPosition;

public interface CoinPositionRepository extends MongoRepository<CoinPosition, String> {
  Optional<CoinPosition> findFirstBySymbolOrderByIdDesc(CoinSymbolEnum symbol);
  Slice<CoinPosition> findBySymbol(CoinSymbolEnum symbol, Pageable pageable);
}

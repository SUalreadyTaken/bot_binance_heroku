package su.binance_bot.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import su.binance_bot.Model.CoinConfig;

public interface CoinConfigRepository extends MongoRepository<CoinConfig, String> {
}

package su.binance_bot.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import su.binance_bot.Model.Idle;

public interface IdleRepository extends MongoRepository<Idle, String> {
}

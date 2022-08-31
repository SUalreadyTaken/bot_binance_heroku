package su.binance_bot.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import su.binance_bot.Model.ChatID;

public interface ChatIDRepository extends MongoRepository<ChatID, String> {}

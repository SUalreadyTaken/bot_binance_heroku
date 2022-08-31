package su.binance_bot.Service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import su.binance_bot.Model.ChatID;
import su.binance_bot.Repository.ChatIDRepository;

@Service
@Transactional
public class ChatIDService {

  private final ChatIDRepository chatIDRepository;

  public ChatIDService(ChatIDRepository chatIDRepository) {
    this.chatIDRepository = chatIDRepository;
  }
  
  public List<ChatID> getChatIds() {
    return this.chatIDRepository.findAll();
  }

  public void saveChat(int chatId) {
    this.chatIDRepository.insert(new ChatID(chatId));
  }

  public void removeChat(int chatId) {
    List<ChatID> all = this.chatIDRepository.findAll();
    for (ChatID c : all) {
      if (c.getChatId() == chatId) {
        this.chatIDRepository.delete(c);
        break;
      }
    }
  }
}

package su.binance_bot;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import su.binance_bot.Model.ChatID;
import su.binance_bot.Service.ChatIDService;

@Component
public class ChatList {
  private List<ChatID> chatIds = new ArrayList<>();
  private final ChatIDService chatIDService;

  public ChatList(ChatIDService chatIDService) {
    this.chatIDService = chatIDService;
  }

  @PostConstruct
  private void initChatIDs () {
    this.chatIds = this.chatIDService.getChatIds();
  }

  public List<ChatID> getChatIds() {
    return this.chatIds;
  }
}

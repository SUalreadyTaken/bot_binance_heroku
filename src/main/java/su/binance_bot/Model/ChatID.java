package su.binance_bot.Model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "chat_id")
public class ChatID {
  
  @Id
  private String id;
  @Indexed(unique=true)
  private int chatId;

  public ChatID(int chatId) {
    this.chatId = chatId;
  }

  public String getId() {
    return this.id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public int getChatId() {
    return this.chatId;
  }

  public void setChatId(int chatId) {
    this.chatId = chatId;
  }

}

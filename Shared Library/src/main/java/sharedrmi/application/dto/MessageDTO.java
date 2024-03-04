package sharedrmi.application.dto;

import java.io.Serializable;

public class MessageDTO implements Serializable {
    private final String messageTopic;
    private final String messageTitle;
    private final String messageText;
    private final long expirationDays;
    private final long timestamp;

    public MessageDTO(String messageTopic, String messageTitle, String messageText, long expirationDays, long timestamp) {
        this.messageTopic = messageTopic;
        this.messageTitle = messageTitle;
        this.messageText = messageText;
        this.expirationDays = expirationDays;
        this.timestamp = timestamp;
    }

    public static MessageDTO.MessageDTOBuilder builder() {
        return new MessageDTO.MessageDTOBuilder();
    }

    public String getMessageTopic() {
        return this.messageTopic;
    }

    public String getMessageTitle() {
        return this.messageTitle;
    }

    public String getMessageText() {
        return this.messageText;
    }

    public long getExpirationDays() {
        return expirationDays;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public static class MessageDTOBuilder {
        private String messageTopic;
        private String messageTitle;
        private String messageText;
        private long expirationDays;
        private long timestamp;

        MessageDTOBuilder() {
        }

        public MessageDTO.MessageDTOBuilder messageTopic(String messageTopic) {
            this.messageTopic = messageTopic;
            return this;
        }

        public MessageDTO.MessageDTOBuilder messageTitle(String messageTitle) {
            this.messageTitle = messageTitle;
            return this;
        }

        public MessageDTO.MessageDTOBuilder messageText(String messageText) {
            this.messageText = messageText;
            return this;
        }

        public MessageDTO.MessageDTOBuilder expirationDays(long expirationDays) {
            this.expirationDays = expirationDays;
            return this;
        }

        public MessageDTO.MessageDTOBuilder timestamp(long timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public MessageDTO build() {
            return new MessageDTO(this.messageTopic, this.messageTitle, this.messageText, this.expirationDays, this.timestamp);
        }

        public String toString() {
            return "MessageDTO.MessageDTOBuilder(messageTopic=" + this.messageTopic + ", messageTitle=" + this.messageTitle + ", messageText=" + this.messageText + ", expirationDays=" + this.expirationDays + ", timestamp=" + this.timestamp + ")";
        }

    }
}

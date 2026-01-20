-- -----------------------------------------------------
-- Table: Chats
-- Description: Represents a single conversation thread (direct or group).
-- -----------------------------------------------------
CREATE TABLE chats (
    chat_id CHAR(36) PRIMARY KEY NOT NULL COMMENT 'Chat ID (UUID)',
    type VARCHAR(10) NOT NULL CHECK (type IN ('direct', 'group')) COMMENT 'Chat type: direct or group',
    name VARCHAR(255) COMMENT 'Group name (NULL for direct chats)',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Creation timestamp',
    last_message_id CHAR(36) COMMENT 'Pointer to the most recent message for fast display'
) COMMENT = 'Chat conversations table';

-- -----------------------------------------------------
-- Table: Messages
-- Description: Stores the actual chat messages.
-- -----------------------------------------------------
CREATE TABLE messages (
    message_id CHAR(36) PRIMARY KEY NOT NULL COMMENT 'Message ID (UUID)',
    chat_id CHAR(36) NOT NULL COMMENT 'Reference to chat',
    sender_id BIGINT NOT NULL COMMENT 'Canvas user ID of sender',
    content TEXT COMMENT 'The message content (text, or path/reference for media)',
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Message timestamp',
    message_type VARCHAR(10) NOT NULL CHECK (message_type IN ('text', 'image', 'video', 'system')) COMMENT 'Message type',
    reply_to_message_id CHAR(36) COMMENT 'For threading/replies (optional)',
    FOREIGN KEY (chat_id) REFERENCES Chats(chat_id) ON DELETE CASCADE,
    FOREIGN KEY (reply_to_message_id) REFERENCES Messages(message_id) ON DELETE SET NULL
) COMMENT = 'Chat messages table';

-- Optimization: Index for fast message retrieval by conversation, ordered by time.
CREATE INDEX idx_messages_chat_time ON Messages (chat_id, timestamp);

-- -----------------------------------------------------
-- Table: Participants
-- Description: Maps users to the chats they belong to (Group Membership).
-- -----------------------------------------------------
CREATE TABLE participants (
    chat_id CHAR(36) NOT NULL COMMENT 'Reference to chat',
    user_id BIGINT NOT NULL COMMENT 'Canvas user ID',
    joined_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'When user joined the chat',
    role VARCHAR(20) NOT NULL DEFAULT 'member' CHECK (role IN ('member', 'admin', 'creator')) COMMENT 'User role in chat',
    last_read_message_id CHAR(36) COMMENT 'The ID of the last message the user read in this chat (Crucial for unread count)',
    PRIMARY KEY (chat_id, user_id),
    FOREIGN KEY (chat_id) REFERENCES Chats(chat_id) ON DELETE CASCADE,
    FOREIGN KEY (last_read_message_id) REFERENCES Messages(message_id) ON DELETE SET NULL
) COMMENT = 'Chat participants table';

-- Optimization: Index for fast lookup of a user's chats
CREATE INDEX idx_participants_user_id ON Participants (user_id);

-- -----------------------------------------------------
-- Constraint Update (Link Chats.last_message_id)
-- Must be done after Messages table is created
-- -----------------------------------------------------
ALTER TABLE Chats
ADD CONSTRAINT fk_chats_last_message
FOREIGN KEY (last_message_id) REFERENCES Messages(message_id) ON DELETE SET NULL;


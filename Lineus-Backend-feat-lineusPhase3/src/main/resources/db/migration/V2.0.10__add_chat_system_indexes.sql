-- Optimize chat system queries
-- 1) Participants filtering by user/chat with deleted flag
CREATE INDEX idx_participants_user_deleted ON participants (user_id, deleted);
CREATE INDEX idx_participants_chat_deleted ON participants (chat_id, deleted);

-- 2) Messages unread/count queries filter on chat_id, sender_id, timestamp
CREATE INDEX idx_messages_chat_sender_time ON messages (chat_id, sender_id, timestamp);

-- 3) Invite link lookup for group chats
CREATE UNIQUE INDEX idx_chats_invite_link_unique ON chats (invite_link);


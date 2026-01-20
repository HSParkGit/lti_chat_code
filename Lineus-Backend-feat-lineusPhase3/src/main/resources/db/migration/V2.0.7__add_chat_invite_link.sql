-- Add invite_link column to Chats table for group chats
ALTER TABLE chats 
ADD COLUMN invite_link VARCHAR(255) COMMENT 'Invite link token for group chat (NULL for direct chats)';

-- Add index for fast lookup by invite link
CREATE INDEX idx_chats_invite_link ON Chats(invite_link);


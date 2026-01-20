-- Soft-delete per participant: hide a chat for a specific user without removing others
ALTER TABLE participants
ADD COLUMN deleted BOOLEAN NOT NULL DEFAULT FALSE COMMENT 'If true, chat is hidden for this participant';


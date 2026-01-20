-- Add mute flag per participant to control WebSocket notifications
ALTER TABLE participants
ADD COLUMN muted BOOLEAN NOT NULL DEFAULT FALSE COMMENT 'If true, user will not receive WebSocket pushes for this chat';


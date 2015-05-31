/*
-- Query: SELECT * FROM messages WHERE user_id = (SELECT id FROM users WHERE name = 'garik') AND date LIKE ('30-05-2015%')
LIMIT 0, 1000

-- Date: 2015-05-30 20:45
*/
INSERT INTO `messages` (`id`,`text`,`date`,`user_id`) VALUES (3,'whats up nigga?','30-05-2015,17:30:05',7);
INSERT INTO `messages` (`id`,`text`,`date`,`user_id`) VALUES (6,'SWAG','30-05-2015,17:32:30',7);

/*
-- Query: SELECT * FROM messages WHERE user_id = (SELECT id FROM users WHERE name = 'alina') AND text LIKE ('%hello%')
LIMIT 0, 1000

-- Date: 2015-05-30 20:47
*/
INSERT INTO `messages` (`id`,`text`,`date`,`user_id`) VALUES (2,'hello','30-05-2015,17:30:02',3);

insert into game_session (game, session_date) values
    (1, '2017/01/01 15:22:22'),
    (2,'2017/02/02 16:22:22'),
    (3,'2017/03/03 12:22:22'),
    (4,'2017/04/04 15:23:23'),
    (5, '2017/05/05 15:12:12'),
    (6,'2017/06/06 11:22:22');
insert into winner values
(1, 1),
(2,2),
(3,3),
(4,1),
(5, 1),
(6,2);
insert into loser values
(1, 2),
(1, 3),
(1, 4),
(2,3),
(2,1),
(3,4),
(3,5),
(3,1),
(4,2),
(4,3),
(4,5),
(5,2),
(5,3),
(5,4),
(6,1),
(6,3),
(6,4),
(6,5);
insert into traitor values
(1,2),
(2,2);
--CREATE DATABASE bradgardendb

CREATE TABLE IF NOT EXISTS member
(
  member_id    SMALLSERIAL PRIMARY KEY UNIQUE NOT NULL,
  first_name   VARCHAR(30) NOT NULL,
  last_name    VARCHAR(30) NOT NULL
);

CREATE TABLE IF NOT EXISTS game
(
  game_id     SMALLSERIAL PRIMARY KEY UNIQUE NOT NULL,
  game_name   VARCHAR(50) NOT NULL,
  max_players INTEGER NOT NULL,
  traitor     BOOLEAN NOT NULL,
  co_op       BOOLEAN NOT NULL
);

CREATE TABLE IF NOT EXISTS game_session
(
  session_id     SMALLSERIAL UNIQUE NOT NULL,
  game           INTEGER REFERENCES game (game_id) NOT NULL,
  session_date   VARCHAR(25) NOT NULL,
  PRIMARY KEY (session_id)
);

CREATE TABLE IF NOT EXISTS traitor
(
  game_session INTEGER REFERENCES game_session (session_id) ON DELETE CASCADE NOT NULL,
  member    INTEGER REFERENCES member (member_id) NOT NULL,
  PRIMARY KEY (game_session,member)
);

CREATE TABLE IF NOT EXISTS loser
(
  game_session  INTEGER REFERENCES game_session (session_id) ON DELETE CASCADE NOT NULL,
  member    INTEGER REFERENCES member (member_id) NOT NULL,
  PRIMARY KEY (game_session,member)
);
CREATE TABLE IF NOT EXISTS winner
(
  game_session  INTEGER REFERENCES game_session (session_id) ON DELETE CASCADE NOT NULL,
  member    INTEGER REFERENCES member (member_id) NOT NULL,
  PRIMARY KEY (game_session,member)
);

CREATE TABLE IF NOT EXISTS api_user
(
    name VARCHAR(50) NOT NULL,
    secret VARCHAR(100) NOT NULL,
    PRIMARY KEY (name)
);

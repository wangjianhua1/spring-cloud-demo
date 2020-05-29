CREATE TABLE IF NOT EXISTS user (

  id       INT primary key NOT NULL auto_increment,

  username VARCHAR(32)     NOT NULL

)  ENGINE = InnoDB  DEFAULT CHARSET = utf8;

INSERT INTO user (username) VALUES ("test"), ("hello world");

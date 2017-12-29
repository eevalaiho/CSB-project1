CREATE TABLE accounts (
    id int PRIMARY KEY,
    name varchar(200),
    iban varchar(200),
    balance double
);

CREATE TABLE messages (
    type varchar(3),
    title varchar(200),
    content varchar(2000)
);

CREATE TABLE credentials (
    username varchar(50),
    password varchar(50)
);

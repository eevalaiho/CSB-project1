CREATE TABLE accounts (
    id int PRIMARY KEY,
    name varchar(200),
    iban varchar(200),
    balance double
);

CREATE TABLE messages (
    title varchar(200),
    content varchar(2000)
);


INSERT INTO users (username, email, password, first_name, last_name, phone_number, gender)
VALUES
    ('user', 'user@example.com', '$2a$10$whapi6SNd8Kcgz/Xffazbe5G8DW9x2Ov0RH1c62/4oWJ3m/P67ZPi', 'User', 'Userov', '828344285', 'MALE'),
    ('admin', 'admin@example.com', '$2a$10$whapi6SNd8Kcgz/Xffazbe5G8DW9x2Ov0RH1c62/4oWJ3m/P67ZPi', 'Admin', 'Adminov', '828344285', 'MALE'),
    ('moderator', 'moderator@example.com', '$2a$10$whapi6SNd8Kcgz/Xffazbe5G8DW9x2Ov0RH1c62/4oWJ3m/P67ZPi', 'Moderator', 'Moderator', '828344285', 'FEMALE');

INSERT INTO user_roles(id, user_role)
VALUES
    (1, 'USER'),
    (2, 'MODERATOR'),
    (3, 'ADMIN');

INSERT INTO users_user_roles(user_entity_id, user_roles_id)
VALUES (1, 1),
       (2, 3),
       (3, 2);

INSERT INTO pictures(url)
VALUES
    ("n/a"),
    ("n/a"),
    ("n/a");

INSERT INTO brands (id, `name`, estimated_at)
VALUES (1, 'Gucci', '1998-01-03'),
       (2, 'Guess', '1986-02-05'),
       (3, 'Nike', '1789-07-09'),
       (4, 'Caesare Paciotti', '1892-08-09'),
       (5, 'Louis Vuitton', '1896-09-06');
INSERT INTO items (id, `type`, manufacture_year, picture_id, brand_id, `size`)
VALUES (1, 'T_SHIRT', 1976, 2, 1, 'MEDIUM'),
       (2, 'TROUSERS', 1968, 1, 1, 'MEDIUM'),
       (3, 'CREW_NECK', 1999, 3, 2, 'MEDIUM');



INSERT INTO offers (id, item_id, `name`, seller_id, price)
VALUES
    (1, 2, 'TROUSERZZZZ', 1, 2601.99),
    (2, 1, 'Tee YEAH', 1, 4000.99),
    (3, 3, 'CREW_NECK BABY', 2, 4000.99),
    (4, 1, 'Tee YEAH', 2, 4000.99),
    (5, 2, 'TROUSERZZZZ', 3, 4000.99),
    (6, 3, 'CREW_NECK BABY', 2, 4000.99),
    (7, 3, 'CREW_NECK BABY', 1, 4000.99),
    (8, 3, 'CREW_NECK BABY', 1, 4000.99),
    (9, 3, 'CREW_NECK BABY', 1, 4000.99);
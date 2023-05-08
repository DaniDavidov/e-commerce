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
    ("https://res.cloudinary.com/du90acnb6/image/upload/v1681020560/1ddb32b8-6448-4200-8bfe-a4bbd751d2c7.jpg"),
    ("https://res.cloudinary.com/du90acnb6/image/upload/v1681020515/cdfe8a3d-0287-4414-8eaa-a2f747aea06d.jpg"),
    ("https://res.cloudinary.com/du90acnb6/image/upload/v1681020038/9b1bb3ff-3f46-42b6-ba08-051ba179a52f.jpg"),
    ("https://res.cloudinary.com/du90acnb6/image/upload/v1680950053/f7ec48cd-67c7-4534-a012-d3f79235366b.jpg"),
    ("https://res.cloudinary.com/du90acnb6/image/upload/v1681021080/6cf4358e-498e-418e-914f-1a88c03fc519.jpg");

INSERT INTO brands (id, `name`, estimated_at)
VALUES (1, 'Gucci', '1998-01-03'),
       (2, 'Guess', '1986-02-05'),
       (3, 'Nike', '1789-07-09'),
       (4, 'Caesare Paciotti', '1892-08-09'),
       (5, 'Louis Vuitton', '1896-09-06');



INSERT INTO offers (id, `type`, manufacture_year, picture_id, brand_id, `size`, `name`, seller_id, price, is_approved, quantity)
VALUES
    (1, 'TROUSERS', 1968, 4, 1, 'MEDIUM', 'TROUSERZZZZ', 1, 2601.99, 0, 1),
    (2, 'T_SHIRT', 1976, 1, 1, 'MEDIUM', 'Tee YEAH', 1, 4000.99, 0, 2),
    (3, 'CREW_NECK', 1999, 5, 2, 'MEDIUM', 'CREW_NECK BABY', 2, 4000.99, 1, 3),
    (4, 'T_SHIRT', 1976, 1, 1, 'MEDIUM', 'Tee YEAH', 2, 4000.99, 0, 4),
    (5, 'TROUSERS', 1968, 4, 1, 'MEDIUM', 'TROUSERZZZZ', 3, 4000.99, 1, 5),
    (6, 'CREW_NECK', 1999, 5, 2, 'MEDIUM', 'CREW_NECK BABY', 2, 4000.99, 1, 6),
    (7, 'CREW_NECK', 1999, 5, 2, 'MEDIUM', 'CREW_NECK BABY', 1, 4000.99, 1, 7),
    (8, 'CREW_NECK', 1999, 5, 2, 'MEDIUM', 'CREW_NECK BABY', 1, 4000.99, 0, 8),
    (9, 'CREW_NECK', 1999, 5, 2, 'MEDIUM', 'CREW_NECK BABY', 1, 4000.99, 0, 6);
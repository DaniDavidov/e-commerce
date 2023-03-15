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
    ('https://www.google.com/imgres?imgurl=https%3A%2F%2Fwww.fjallraven.com%2F4aad59%2Fglobalassets%2Fcatalogs%2Ffjallraven%2Ff8%2Ff866%2Ff86666%2Ff620%2Fvardag_trousers_m_86666-620_a_main_fjr.jpg%3Fwidth%3D2000%26height%3D2000%26mode%3DBoxPad%26bgcolor%3Dfff%26quality%3D80&imgrefurl=https%3A%2F%2Fwww.fjallraven.com%2Fuk%2Fen-gb%2Fmen%2Ftrousers%2Foutdoor-trousers%2Fvardag-trousers-m&tbnid=uco7iWqpCPHhGM&vet=12ahUKEwjAkfXektv9AhXEn_0HHbTEAp8QMygBegUIARDDAQ..i&docid=-LATlofTDb9gSM&w=2000&h=2000&q=trousers&ved=2ahUKEwjAkfXektv9AhXEn_0HHbTEAp8QMygBegUIARDDAQ'),
    ('https://www.google.com/imgres?imgurl=https%3A%2F%2Fzajo.bwcdn.net%2Fmedia%2F2022%2F06%2F1%2F5%2Fsk-leon-t-shirt-ss-15869-size-large-v-2.jpg&imgrefurl=https%3A%2F%2Fwww.zajo.net%2Fbg%2Fudobna-m-zka-teniska-ot-ekologicen-tencel-leon-t-shirt-ss-10659&tbnid=_hsFjrSVtsb4xM&vet=12ahUKEwjF19eMk9v9AhXj9rsIHfeKCAUQMygAegUIARDBAQ..i&docid=Ek_zMD6sy6MVcM&w=1000&h=1000&q=t-shirt&ved=2ahUKEwjF19eMk9v9AhXj9rsIHfeKCAUQMygAegUIARDBAQ'),
    ('https://www.google.com/imgres?imgurl=https%3A%2F%2Fcdn-images.farfetch-contents.com%2F16%2F81%2F12%2F98%2F16811298_33349451_600.jpg&imgrefurl=https%3A%2F%2Fwww.farfetch.com%2Fbg%2Fshopping%2Fmen%2Facne-studios-face-patch-crew-neck-sweatshirt-item-16811298.aspx&tbnid=R90PpY1_9sQywM&vet=12ahUKEwi3ibqmk9v9AhUZVeUKHXqoAhsQMygGegUIARDUAQ..i&docid=JKibbOK0sbntyM&w=600&h=801&q=crew%20neck&ved=2ahUKEwi3ibqmk9v9AhUZVeUKHXqoAhsQMygGegUIARDUAQ');

INSERT INTO brands (id, `name`, image_url)
VALUES (1, 'Gucci', 'https://www.google.com/url?sa=i&url=https%3A%2F%2Fluxuryheterotopia.wordpress.com%2F2017%2F03%2F27%2Fguccis-brand-value%2F&psig=AOvVaw2faidd8GabS2Q8v2ljYumv&ust=1678614783737000&source=images&cd=vfe&ved=0CBAQjRxqFwoTCPia8cHN0_0CFQAAAAAdAAAAABAE'),
       (2, 'Guess', 'https://www.google.com/url?sa=i&url=https%3A%2F%2Fwww.guess.com%2F&psig=AOvVaw1aqTiNkfxJP3pHwQUixKgK&ust=1678614847510000&source=images&cd=vfe&ved=0CBAQjRxqFwoTCJCHmODN0_0CFQAAAAAdAAAAABAE'),
       (3, 'Nike', 'https://www.google.com/imgres?imgurl=https%3A%2F%2Fstatic.nike.com%2Fa%2Fimages%2Ff_jpg%2Cq_auto%3Aeco%2F61b4738b-e1e1-4786-8f6c-26aa0008e80b%2Fswoosh-logo-black.png&imgrefurl=https%3A%2F%2Fwww.nike.com%2F&tbnid=FP5YR06q7cTu1M&vet=12ahUKEwj0qdjpzdP9AhXNjv0HHfb_ALQQMygBegUIARC-AQ..i&docid=mgtROrdDu1XGJM&w=1600&h=1600&itg=1&q=nike%20brand&ved=2ahUKEwj0qdjpzdP9AhXNjv0HHfb_ALQQMygBegUIARC-AQ'),
       (4, 'Caesare Paciotti', 'https://www.google.com/imgres?imgurl=https%3A%2F%2Flookaside.fbsbx.com%2Flookaside%2Fcrawler%2Fmedia%2F%3Fmedia_id%3D1296659100399250&imgrefurl=https%3A%2F%2Fwww.facebook.com%2FCesarePaciottiOfficial%2F&tbnid=C-zm8gI3iuBkyM&vet=12ahUKEwiVpqv-zdP9AhVn4bsIHfXnBKwQMygBegUIARC4AQ..i&docid=SvIBSR9Qs13mtM&w=1000&h=1000&q=cesare%20pacciotti%20brand&ved=2ahUKEwiVpqv-zdP9AhVn4bsIHfXnBKwQMygBegUIARC4AQ'),
       (5, 'Louis Vuitton', 'https://www.google.com/imgres?imgurl=https%3A%2F%2Fawards.brandingforum.org%2Fwp-content%2Fuploads%2F2017%2F12%2FLouis-Vuitton-Logo.jpg&imgrefurl=https%3A%2F%2Fawards.brandingforum.org%2Fbrands%2Flouis-vuitton%2F&tbnid=SqN9RBPAjzqqHM&vet=12ahUKEwi6iuqYztP9AhWlnP0HHbyrBAwQMygBegUIARC8AQ..i&docid=9aH_gCGfscCbfM&w=368&h=366&q=louis%20vuitton%20brand&ved=2ahUKEwi6iuqYztP9AhWlnP0HHbyrBAwQMygBegUIARC8AQ');
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
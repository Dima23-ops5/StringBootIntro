INSERT INTO users (id, email, password, first_name, last_name, shipping_address)
VALUES (1, 'alice@gmail.com', 'alice1234', 'Alice', 'Kingsley', 'Khreshchatyk street 53');

INSERT INTO shopping_carts (id, user_id) VALUES (1, 1);

INSERT INTO books (id, title, author, isbn, price, description, cover_image)
VALUES (1, 'Harry Potter', 'Joanne Rowling', '12345', 50.00, 'Book about boy...', 'img.harry.potter');

INSERT INTO cart_items (id, shopping_cart_id, book_id, quantity)
VALUES (1, 1, 1, 5);

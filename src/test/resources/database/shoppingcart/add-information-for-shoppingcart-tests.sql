INSERT INTO users (id, email, password, first_name, last_name, shipping_address)
VALUES (1, 'alice@gmail.com', 'alice1234', 'Alice', 'Kingsley', 'Khreshchatyk street 53');

INSERT INTO books (id, title, author, isbn, price, description, cover_image)
VALUES (1, 'Harry Potter', 'Joanne Rowling', '12345', 50.00, 'Book about boy...', 'img.harry.potter');

INSERT INTO books (id, title, author, isbn, price, description, cover_image)
VALUES (2, 'The little prince', 'Antoine de Saint-Exupery', '4557839', 80.00, 'Story about little prince', 'img.little.prince');

INSERT INTO books (id, title, author, isbn, price, description, cover_image)
VALUES (3, 'The Hobbit', 'J. R. R. Tolkien', '93587', 40.00, 'Book about hobbit Bilbo', 'img.hobbit');

INSERT INTO categories (id, name, description)
VALUES (1, 'Fantasy', 'Fantasy is a genre of speculative fiction involving magical elements...');

INSERT INTO categories (id, name, description)
VALUES (2, 'Horror', 'Novels are characterized by the fact that the main plot revolves around something scary and terrifying.');

INSERT INTO books_categories (book_id, category_id)
VALUES (1, 1), (2, 1), (3, 2);

INSERT INTO shopping_carts (id, user_id)
VALUES (1, 1);

INSERT INTO cart_items (id, shopping_cart_id, book_id, quantity)
VALUES (1, 1, 1, 5);

INSERT INTO roles (id, role)
VALUES (1, "USER");

INSERT INTO users_roles (user_id, role_id)
VALUES (1, 1);

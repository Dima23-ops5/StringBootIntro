INSERT INTO books (id, title, author, isbn, price, description, coverImage)
VALUES (1, 'Harry Potter', 'Joanne Rowling', '12345', 50.00, 'Book about boy...', 'img');

INSERT INTO categories (id, name, description)
VALUES (1, 'fantasy', 'fantastic books');

INSERT INTO books_categories (book_id, category_id)
VALUES (1, 1);

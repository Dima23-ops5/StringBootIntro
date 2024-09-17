DELETE FROM books_categories WHERE book_id = 1;
DELETE FROM books WHERE id = 1;
DELETE FROM categories WHERE id = 4;

INSERT INTO books (id, title, author, isbn, price, description, cover_image)
VALUES (1, 'Harry Potter', 'Joanne Rowling', '12345', 50.00, 'Book about boy...', 'img');

INSERT INTO categories (id, name, description)
VALUES (4, 'fantasy', 'fantastic books');

INSERT INTO books_categories (book_id, category_id)
VALUES (1, 4);
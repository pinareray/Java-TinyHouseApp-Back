-- 1. Reservation tablosuna created_at ve updated_at alanları ekleniyor
ALTER TABLE reservations
    ADD created_at DATE DEFAULT GETDATE();

ALTER TABLE reservations
    ADD updated_at DATE;

-- 2. Payment tablosuna user_id alanı ekleniyor
ALTER TABLE payments
    ADD user_id INT;

-- 3. user_id için foreign key constraint ekleniyor
ALTER TABLE payments
    ADD CONSTRAINT fk_payments_user FOREIGN KEY (user_id) REFERENCES users(id);

CREATE TABLE users (
                       id INT IDENTITY(1, 1) PRIMARY KEY,
                       first_name VARCHAR(50) NOT NULL,
                       last_name VARCHAR(50) NOT NULL,
                       email VARCHAR(255) NOT NULL UNIQUE,
                       password_hash VARCHAR(255),
                       role VARCHAR(50) CHECK (role IN ('ADMIN', 'HOST', 'RENTER')),
                       is_active BIT DEFAULT 1
);
CREATE TABLE houses (
                        id INT IDENTITY(1, 1) PRIMARY KEY,
                        title VARCHAR(100) NOT NULL,
                        description VARCHAR(1000),
                        price FLOAT NOT NULL,
                        location VARCHAR(255) NOT NULL,
                        status VARCHAR(50),
                        host_id INT,
                        CONSTRAINT fk_houses_host FOREIGN KEY (host_id) REFERENCES users(id)
);
CREATE TABLE reservations (
                              id INT IDENTITY(1, 1) PRIMARY KEY,
                              start_date DATE NOT NULL,
                              end_date DATE NOT NULL,
                              status VARCHAR(50),
                              renter_id INT,
                              house_id INT,
                              CONSTRAINT fk_reservations_renter FOREIGN KEY (renter_id) REFERENCES users(id),
                              CONSTRAINT fk_reservations_house FOREIGN KEY (house_id) REFERENCES houses(id)
);
CREATE TABLE payments (
                          id INT IDENTITY(1, 1) PRIMARY KEY,
                          amount FLOAT NOT NULL,
                          payment_date DATETIME,
                          status VARCHAR(50),
                          reservation_id INT UNIQUE,
                          CONSTRAINT fk_payments_reservation FOREIGN KEY (reservation_id) REFERENCES reservations(id)
);
CREATE TABLE comments (
                          id INT IDENTITY(1, 1) PRIMARY KEY,
                          content VARCHAR(1000),
                          rating INT,
                          user_id INT,
                          house_id INT,
                          CONSTRAINT fk_comments_user FOREIGN KEY (user_id) REFERENCES users(id),
                          CONSTRAINT fk_comments_house FOREIGN KEY (house_id) REFERENCES houses(id)
);

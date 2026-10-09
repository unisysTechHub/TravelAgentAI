-- Table creation
CREATE TABLE contacts (
      id SERIAL PRIMARY KEY,
      name VARCHAR(100) NOT NULL,
      email VARCHAR(150) NOT NULL,
      city VARCHAR(50) NOT NULL
);

-- Sample data
INSERT INTO contacts (name, email, city) VALUES
     ('Alice Dupont', 'alice.paris@example.com', 'Paris'),
     ('Marc Laurent', 'marc.paris@example.com', 'Paris'),
     ('Sakura Tanaka', 'sakura.tokyo@example.com', 'Tokyo'),
     ('Hiroshi Yamamoto', 'hiroshi.tokyo@example.com', 'Tokyo'),
     ('Rashid Al-Farsi', 'rashid.dubai@example.com', 'Dubai'),
     ('Laila Hassan', 'laila.dubai@example.com', 'Dubai'),
     ('Jean-Pierre Martin', 'jean.marseille@example.com', 'Marseille'),
     ('Keiko Nakamura', 'keiko.kyoto@example.com', 'Kyoto'),
     ('Fatima Al-Khalifa', 'fatima.abudhabi@example.com', 'Abu Dhabi'),
     ('Yuki Sato', 'yuki.osaka@example.com', 'Osaka');
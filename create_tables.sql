CREATE TABLE product (
    id INT PRIMARY KEY AUTO_INCREMENT,
    product_name VARCHAR(255) NOT NULL,
    product_desc TEXT,
    cost_price DECIMAL(10, 2),
    selling_price DECIMAL(10, 2),
    image_urls JSON
);

CREATE TABLE color (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL
);

CREATE TABLE size (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL
);

CREATE TABLE product_variation (
    id INT PRIMARY KEY AUTO_INCREMENT,
    product_id INT,
    color_id INT,
    size_id INT,
    quantity INT,
    FOREIGN KEY (product_id) REFERENCES product(id),
    FOREIGN KEY (color_id) REFERENCES color(id),
    FOREIGN KEY (size_id) REFERENCES size(id)
);

CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    full_name VARCHAR(100),
    registration_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_login TIMESTAMP,
);

CREATE TABLE sales (
    id INT PRIMARY KEY AUTO_INCREMENT,
    product_variation_id INT,
    quantity_sold INT,
    total_revenue DECIMAL(10, 2),
    total_cost DECIMAL(10, 2),
    total_profit DECIMAL(10, 2),
    FOREIGN KEY (product_variation_id) REFERENCES product_variation(id)
);

-- parking_slots table
CREATE TABLE IF NOT EXISTS parking_slots (
    id INT AUTO_INCREMENT PRIMARY KEY,
    slot_number VARCHAR(10) NOT NULL UNIQUE,
    is_occupied BOOLEAN DEFAULT FALSE
);

-- vehicles table
CREATE TABLE IF NOT EXISTS vehicles (
    id INT AUTO_INCREMENT PRIMARY KEY,
    vehicle_number VARCHAR(20) NOT NULL,
    entry_time DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- vehicle_exit table
CREATE TABLE IF NOT EXISTS vehicle_exit (
    id INT AUTO_INCREMENT PRIMARY KEY,
    vehicle_id INT NOT NULL,
    vehicle_number VARCHAR(20),
    exit_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    total_hours INT,
    total_charge DECIMAL(10, 2),
    FOREIGN KEY (vehicle_id) REFERENCES vehicles(id)
);
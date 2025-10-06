-- Crime Records Management System Database Setup for MySQL

-- Create database
CREATE DATABASE IF NOT EXISTS crms_db;
USE crms_db;

-- Create officers table
CREATE TABLE IF NOT EXISTS officers (
    officerid VARCHAR(20) PRIMARY KEY,
    officername VARCHAR(100) NOT NULL,
    password VARCHAR(50) NOT NULL,
    designation VARCHAR(50) NOT NULL,
    station VARCHAR(100) NOT NULL,
    age INT NOT NULL,
    phno BIGINT NOT NULL
);

-- Create FIR table
CREATE TABLE IF NOT EXISTS fir (
    firno VARCHAR(20) PRIMARY KEY,
    complainant_name VARCHAR(100) NOT NULL,
    complainant_address TEXT,
    complainant_phone BIGINT,
    incident_date DATE,
    incident_location VARCHAR(200),
    incident_description TEXT,
    officer_id VARCHAR(20),
    status VARCHAR(50) DEFAULT 'PENDING',
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (officer_id) REFERENCES officers(officerid)
);

-- Create cases table
CREATE TABLE IF NOT EXISTS cases (
    case_id VARCHAR(20) PRIMARY KEY,
    fir_no VARCHAR(20),
    assigned_officer VARCHAR(20),
    case_status VARCHAR(50) DEFAULT 'OPEN',
    assigned_date DATE,
    FOREIGN KEY (fir_no) REFERENCES fir(firno),
    FOREIGN KEY (assigned_officer) REFERENCES officers(officerid)
);

-- Insert sample officers
INSERT IGNORE INTO officers VALUES 
('ADM001', 'Admin Officer', 'admin123', 'Administrator', 'Central Station', 35, 9876543210),
('HIG001', 'John Smith', 'pass123', 'Inspector', 'North Station', 40, 9876543211),
('HIG002', 'Sarah Johnson', 'pass456', 'Sub-Inspector', 'South Station', 38, 9876543212),
('LOW001', 'Mike Wilson', 'pass789', 'Constable', 'East Station', 28, 9876543213),
('LOW002', 'Lisa Brown', 'pass321', 'Head Constable', 'West Station', 32, 9876543214);

-- Insert sample FIR records
INSERT IGNORE INTO fir VALUES 
('FIR001', 'Rajesh Kumar', '123 Main St, City', 9876543215, '2024-01-15', 'Market Area', 'Theft of mobile phone', 'HIG001', 'UNDER_INVESTIGATION', NOW()),
('FIR002', 'Priya Sharma', '456 Park Ave, City', 9876543216, '2024-01-16', 'Bus Stand', 'Chain snatching incident', 'HIG002', 'PENDING', NOW()),
('FIR003', 'Amit Patel', '789 Garden St, City', 9876543217, '2024-01-17', 'Shopping Mall', 'Vehicle theft', 'LOW001', 'CLOSED', NOW());

COMMIT;

-- Display created tables
SHOW TABLES;
SELECT 'Database setup completed successfully!' as Status;
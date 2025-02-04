CREATE TABLE people (
    pid NUMBER PRIMARY KEY,
    name VARCHAR2(100),
    address VARCHAR2(200),
    phone VARCHAR2(20)
);


INSERT INTO people (pid, name, address, phone) VALUES (1, 'Alice', '123 Main St', '400-1234');
INSERT INTO people (pid, name, address, phone) VALUES (2, 'Bob', '456 raja St', '400-5678');
INSERT INTO people (pid, name, address, phone) VALUES (3, 'Charlie', '789 rani St', '400-9101');
COMMIT;


CREATE TABLE people_changes (
    change_id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    person_id NUMBER REFERENCES people(id),
    column_name VARCHAR2(50),  -- Attribute being changed
    old_value VARCHAR2(200),   -- Previous value
    new_value VARCHAR2(200),   -- Updated value
    valid_from DATE,           -- Start date for the change
    valid_to DATE              -- End date for the change
);



-- Change Alice’s address from Jan 1, 2024, to June 30, 2024
INSERT INTO people_changes (person_id, column_name, old_value, new_value, valid_from, valid_to)
VALUES (1, 'address', '123 Main St', '999 New Ave', DATE '2024-01-01', DATE '2024-06-30');

-- Change Bob’s phone number from Feb 1, 2024, to Dec 31, 2024
INSERT INTO people_changes (person_id, column_name, old_value, new_value, valid_from, valid_to)
VALUES (2, 'phone', '555-5678', '555-7777', DATE '2024-02-01', DATE '2024-12-31');

COMMIT;



CREATE VIEW people_consolidated AS
SELECT 
    p.id,
    COALESCE(
        (SELECT new_value FROM people_changes c 
         WHERE c.person_id = p.id 
         AND c.column_name = 'name' 
         AND SYSDATE BETWEEN c.valid_from AND c.valid_to),
        p.name
    ) AS name,
    COALESCE(
        (SELECT new_value FROM people_changes c 
         WHERE c.person_id = p.id 
         AND c.column_name = 'address' 
         AND SYSDATE BETWEEN c.valid_from AND c.valid_to),
        p.address
    ) AS address,
    COALESCE(
        (SELECT new_value FROM people_changes c 
         WHERE c.person_id = p.id 
         AND c.column_name = 'phone' 
         AND SYSDATE BETWEEN c.valid_from AND c.valid_to),
        p.phone
    ) AS phone
FROM people p;

ALTER TABLE events
    ADD end_time time WITHOUT TIME ZONE;

ALTER TABLE events
    ADD start_time time WITHOUT TIME ZONE;

ALTER TABLE events
    ALTER COLUMN start_date TYPE date USING (start_date::date);

UPDATE events
SET start_date = '2019-12-01'
WHERE start_date IS NULL;
ALTER TABLE events
    ALTER COLUMN start_date SET NOT NULL;

ALTER TABLE events
    ALTER COLUMN end_date TYPE date USING (end_date::date);

UPDATE events
SET end_date = '2020-12-01'
WHERE end_date IS NULL;
ALTER TABLE events
    ALTER COLUMN end_date SET NOT NULL;

ALTER TABLE events
    ALTER COLUMN name TYPE VARCHAR(255) USING (name::VARCHAR(255));

UPDATE events
SET name = 'Any name'
WHERE name IS NULL;
ALTER TABLE events
    ALTER COLUMN name SET NOT NULL;
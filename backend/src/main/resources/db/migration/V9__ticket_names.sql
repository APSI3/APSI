ALTER TABLE tickets ADD holder_first_name varchar(255);
ALTER TABLE tickets ADD holder_last_name varchar(255);

UPDATE tickets SET holder_first_name='Jan', holder_last_name='Kowalski';

ALTER TABLE tickets ALTER COLUMN holder_first_name SET NOT NULL;
ALTER TABLE tickets ALTER COLUMN holder_last_name SET NOT NULL;
INSERT INTO events(name, start_date, end_date, description, organizer_id)
VALUES ('testowe wydarzenie1', '2024-04-18', '2024-04-18', 'testowe wydarzenie dodane w migracji', 3),
('testowe wydarzenie2', '2024-05-18', '2024-05-18', 'testowe wydarzenie dodane w migracji', 3);

INSERT INTO ticket_types(event_id, name, price, quantity_available)
VALUES (1, 'tanie bilety', 9.99, 1000),
(1, 'drogie bilety', 19.99, 999),
(2, 'zwykłe bilety', 9.99, 100);

INSERT INTO tickets(ticket_type_id, holder_id)
VALUES (2, 2);
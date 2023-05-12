

INSERT INTO ticket_service.public.experts (id, name, surname) VALUES (1, 'piero', 'angela');
INSERT INTO ticket_service.public.products (id, name, brand) VALUES (1, 'name', 'brand');

INSERT INTO ticket_service.public.tickets (id, arg, obj, priority, expert_id, product_id, profile_email) VALUES (1, 'argument', 'obj', 1, 1, 9781292204482, 'mariorossi@gmail.com');

INSERT INTO ticket_service.public.message (id, body, chatter, timestamp, ticket_id) VALUES (1, 'body1', 'piero', '1999-01-08 04:05:06', 1);
INSERT INTO ticket_service.public.message (id, body, chatter, timestamp, ticket_id) VALUES (2, 'body2', 'franco', '1999-01-08 04:08:06', 1);

INSERT INTO ticket_service.public.statuses (id, status, status_changer, timestamp, expert_id, ticket_id) VALUES (1, 'progress', 'franco', '1999-01-08 01:05:06', 1, 1);
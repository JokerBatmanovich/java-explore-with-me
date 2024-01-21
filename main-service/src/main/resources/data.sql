insert into users(name, email) values
    ('User1', 'user1@gmail.com'),
    ('User2', 'user2@gmail.com'),
    ('User3', 'user3@gmail.com');

insert into categories(name) values ('Category1');

 insert into events(title, annotation, category_id, description, confirmed_requests,
                    created_on, published_on, event_date, initiator_id, lat, lon,
                    paid, participant_limit, request_moderation, state, views) VALUES
     ('Event1 title', 'Annotation to Event1', 1, 'description1', 0,
      '2024-01-01 10:00:00', '2024-01-01 10:00:00', '2024-01-01 10:00:00', 1, 1, 1, false, 0, false, 'PUBLISHED', 0),

    ('Event2 title', 'Annotation to Event2', 1, 'description2', 0,
     '2024-01-01 10:00:00', '2024-01-01 10:00:00', '2024-01-01 10:00:00', 1, 1, 1, false, 0, false, 'PUBLISHED', 0),

    ('Event3 title', 'Annotation to Event3', 1, 'description1', 0,
     '2024-01-01 10:00:00', '2024-01-01 10:00:00', '2024-01-01 10:00:00', 1, 1, 1, false, 0, false, 'PENDING', 0)
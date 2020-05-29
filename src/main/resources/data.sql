INSERT INTO role VALUES (1,'ROLE_ADMIN'), (2,'ROLE_USER') ON CONFLICT DO NOTHING;

INSERT INTO article_status VALUES (1,'EDITED'),(2,'NOT_EDITED') ON CONFLICT DO NOTHING;

INSERT INTO all_users (id,password,username,role_id) VALUES (2,'$2a$10$Ih1WSBE8.7qF2bhyhPs83OuYjHhX5twEZ97pwPZJXXHi94vwLaYgq','Dilrukshi',1) ON CONFLICT DO NOTHING;

INSERT INTO admin (admin_id,username,password,user_id,email,contact_no) VALUES (1,'Dilrukshi','dilly',2,'dilly@gmail.com','234 432 3456') ON CONFLICT DO NOTHING;

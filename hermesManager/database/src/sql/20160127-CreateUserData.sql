-- script update - postgres --

 -- usuario_web (usuario: admin - password: $$admin$$) Para BCryptPasswordEncoder --
INSERT INTO usuario_web (rol, email, password) VALUES ('ROLE_ADMIN', 'admin', '$2a$10$5dfh2cas5VPqJzlbUMezH.x3UvkEvYGyuYuK18vLSBwWGDhwLUN.S');
-- script update - postgres --

-- usuario_web (usuario:user - password:user) Para MD5 --
INSERT INTO usuario_web (rol, email, password) VALUES
    ('ADMIN', 'user3', '47a733d60998c719cf3526ae7d106d13');
    
 -- usuario_web (usuario:user - password:user) Para BCryptPasswordEncoder --
INSERT INTO usuario_web (rol, email, password) VALUES
    ('ADMIN', 'user3', '$2a$10$WPe2V9MC1loxbiLIsqQE9e4hYnUq37dHpPzKAcv1RVUnfwHiSfJyG');
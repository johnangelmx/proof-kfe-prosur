use
dbkfe;

select *
from ventas;
INSERT INTO usuarios (nombre, email, contrasena, rol)
VALUES ('Juan Perez', 'juan@example.com', 'password123', 'admin'),
       ('Maria Lopez', 'maria@example.com', 'securepass', 'user'),
       ('Pedro Ramirez', 'pedro@example.com', '12345678', 'user'),
       ('Ana Garcia', 'ana@example.com', 'password', 'user'),
       ('Luis Martinez', 'luis@example.com', 'qwerty', 'user'),
       ('Laura Sanchez', 'laura@example.com', 'letmein', 'admin'),
       ('Carlos Rodriguez', 'carlos@example.com', 'password123', 'user'),
       ('Sofia Torres', 'sofia@example.com', 'securepass', 'user'),
       ('Javier Gomez', 'javier@example.com', '12345678', 'user'),
       ('Monica Jimenez', 'monica@example.com', 'password', 'user'),
       ('David Ruiz', 'david@example.com', 'qwerty', 'user'),
       ('Elena Diaz', 'elena@example.com', 'letmein', 'admin');
INSERT INTO productos (nombre, descripcion, precio, cantidad_stock)
VALUES ('Café Americano', 'Café con agua caliente.', 2.50, 100),
       ('Café Latte', 'Café con leche caliente.', 3.00, 80),
       ('Café Mocha', 'Café con leche y chocolate caliente.', 3.50, 70),
       ('Capuchino', 'Café con leche y espuma de leche caliente.', 3.25, 90),
       ('Espresso', 'Café negro y fuerte.', 2.00, 120),
       ('Macchiato', 'Café con una pequeña cantidad de leche caliente.', 2.75, 60),
       ('Café Frappé', 'Café frío mezclado con hielo y leche.', 4.00, 50),
       ('Café con Leche', 'Café con leche caliente.', 3.00, 80),
       ('Café Cortado', 'Café con una pequeña cantidad de leche caliente.', 2.75, 70),
       ('Café con Hielo', 'Café servido sobre hielo.', 3.00, 60),
       ('Café Vienés', 'Café con nata y cacao.', 3.50, 65),
       ('Café Irlandés', 'Café con whisky y nata.', 4.50, 45),
       ('Café Bombón', 'Café con leche condensada.', 3.75, 55),
       ('Café Ristretto', 'Variante del espresso más concentrado.', 2.25, 85),
       ('Café Doppio', 'Doble ración de espresso.', 3.00, 75);

INSERT INTO ventas (id_producto, id_usuario, cantidad, fecha, total)
VALUES (1, 1, 5, '2024-01-05', 12.50),
       (2, 3, 3, '2024-01-08', 7.50);

INSERT INTO ventas (id_producto, id_usuario, cantidad, fecha, total)
VALUES (1, 1, 4, '2024-02-03', 10.00),
       (2, 2, 3, '2024-02-06', 7.50);


INSERT INTO ventas (id_producto, id_usuario, cantidad, fecha, total)
VALUES (1, 1, 6, '2024-03-05', 15.00),
       (2, 3, 4, '2024-03-08', 10.00);


INSERT INTO ventas (id_producto, id_usuario, cantidad, fecha, total)
VALUES (1, 1, 5, '2024-04-03', 12.50),
       (2, 2, 3, '2024-04-06', 7.50);

select * from usuarios;
select * from productos;
select * from ventas;
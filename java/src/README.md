Proyecto de Gestión de Tickets - Ejemplos de SQL
Este documento contiene ejemplos de las consultas SQL clave utilizadas en la capa DAO del proyecto.

1. Crear un Ticket
   Inserta un nuevo registro en la tabla tickets. El id es autogenerado por la base de datos. El assignee_id es NULL por defecto.

INSERT INTO tickets (title, description, reporter_id, category_id, state_id, created_at)
VALUES (?, ?, ?, ?, ?, ?);

2. Asignar un Ticket
   Actualiza el campo assignee_id de un ticket existente.

UPDATE tickets
SET assignee_id = ?
WHERE id = ?;

3. Cambiar el Estado de un Ticket
   Actualiza el campo state_id de un ticket existente. La lógica de negocio en la capa de servicio (TicketService) valida si la transición es permitida antes de ejecutar esta consulta.

UPDATE tickets
SET state_id = ?
WHERE id = ?;

4. Agregar un Comentario
   Inserta un nuevo registro en la tabla comments, asociándolo a un ticket_id.

INSERT INTO comments (ticket_id, user_id, text, created_at)
VALUES (?, ?, ?, ?);

5. Listar Comentarios de un Ticket
   Recupera todos los comentarios asociados a un ticket_id específico, ordenados por fecha de creación.

SELECT * FROM comments
WHERE ticket_id = ?
ORDER BY created_at ASC;

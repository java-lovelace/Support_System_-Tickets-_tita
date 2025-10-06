package domain;

import java.time.LocalDateTime;

public class Comentario {
    private int id;
    private int ticketId;
    private int userId; // El autor del comentario
    private String text;
    private LocalDateTime createdAt;

    public Comentario(int ticketId, int userId, String text) {
        this.ticketId = ticketId;
        this.userId = userId;
        this.text = text;
        this.createdAt = LocalDateTime.now();
    }

    // --- Getters y Setters ---
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getTicketId() { return ticketId; }
    public void setTicketId(int ticketId) { this.ticketId = ticketId; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}


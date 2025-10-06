package domain;

import java.time.LocalDateTime;

public class Ticket {
    private int id;
    private String title;
    private String description;
    private int reporterId;
    private int categoryId;
    private int stateId;
    private Integer assigneeId; // Usamos Integer para que pueda ser null
    private LocalDateTime createdAt;

    // Constructor vacío para mapeo desde JDBC
    public Ticket() {}

    // Constructor para la creación de un nuevo ticket
    public Ticket(String title, String description, int reporterId, int categoryId, int stateId) {
        this.title = title;
        this.description = description;
        this.reporterId = reporterId;
        this.categoryId = categoryId;
        this.stateId = stateId;
        this.createdAt = LocalDateTime.now();
    }

    // --- Getters y Setters ---
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public int getReporterId() { return reporterId; }
    public void setReporterId(int reporterId) { this.reporterId = reporterId; }
    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }
    public int getStateId() { return stateId; }
    public void setStateId(int stateId) { this.stateId = stateId; }
    public Integer getAssigneeId() { return assigneeId; }
    public void setAssigneeId(Integer assigneeId) { this.assigneeId = assigneeId; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    @Override
    public String toString() {
        return "Ticket{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", reporterId=" + reporterId +
                ", assigneeId=" + (assigneeId != null ? assigneeId : "N/A") +
                ", stateId=" + stateId +
                '}';
    }
}


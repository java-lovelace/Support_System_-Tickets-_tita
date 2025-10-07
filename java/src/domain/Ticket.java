package domain;


import java.time.LocalDateTime;

public class Ticket {
    private int id;
    private String title;
    private String description;
    private int reporterId;
    private Integer assigneeId;
    private int categoryId;
    private int stateId;
    private String categoryName;
    private String stateName;
    private String reporterUsername;
    private String assigneeUsername;
    private LocalDateTime createdAt;
  
    //Constructor vacio para la creacion de tickets
    public Ticket() {}
  
    public Ticket(String title, String description, int reporterId, int categoryId, int stateId) {
      this.title = title;
      this.description = description;
      this.reporterId = reporterId;
      this.categoryId = categoryId;
      this.stateId = stateId;
      this.createdAt = LocalDateTime.now();
}


    // Setters y Getters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getReporterId() {
        return reporterId;
    }

    public void setReporterId(int reporterId) {
        this.reporterId = reporterId;
    }

    public int getAssigneeId() {
        return assigneeId;
    }

    public void setAssigneeId(int assigneeId) {
        this.assigneeId = assigneeId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getStateId() {
        return stateId;
    }

    public void setStateId(int stateId) {
        this.stateId = stateId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getReporterUsername() {
        return reporterUsername;
    }

    public void setReporterUsername(String reporterUsername) {
        this.reporterUsername = reporterUsername;
    }

    public String getAssigneeUsername() {
        return assigneeUsername;
    }

    public void setAssigneeUsername(String assigneeUsername) {
        this.assigneeUsername = assigneeUsername;
    }
  
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

 @Override
public String toString() {
    return "Ticket{" +
            "id=" + id +
            ", title='" + title + '\'' +
            ", description='" + description + '\'' +
            ", reporterId=" + reporterId +
            ", reporterUsername='" + reporterUsername + '\'' +
            ", assigneeId=" + (assigneeId != null ? assigneeId : "N/A") +
            ", assigneeUsername='" + assigneeUsername + '\'' +
            ", categoryId=" + categoryId +
            ", categoryName='" + categoryName + '\'' +
            ", stateId=" + stateId +
            ", stateName='" + stateName + '\'' +
            ", createdAt=" + createdAt +
            '}';
    }
}

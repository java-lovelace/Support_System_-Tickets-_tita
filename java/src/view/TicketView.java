package view;

import java.sql.Timestamp;

public class TicketView {
    private int id;
    private String title;
    private String description;
    private String reporterUsername;
    private String assigneeUsername;
    private String categoryName;
    private String stateName;
    private java.sql.Timestamp createdAt;

    public TicketView(int id, String title, String description, String reporterUsername, String assigneeUsername, String categoryName, String stateName, Timestamp createdAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.reporterUsername = reporterUsername;
        this.assigneeUsername = assigneeUsername;
        this.categoryName = categoryName;
        this.stateName = stateName;
        this.createdAt = createdAt;
    }

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

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString(){
        return String.format("%d | %s | %s | reporter=%s | assignee=%s | cat=%s | state=%s | %s",
                id,title,description,reporterUsername,assigneeUsername == null ? "-" : assigneeUsername,
                categoryName,stateName,createdAt);
    }
}

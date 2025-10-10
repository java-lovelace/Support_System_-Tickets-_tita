package domain;

import java.time.LocalDateTime;

public class Usuario {
    private int id;
    private String username;
    private Role role;
    private LocalDateTime createdAt;

    public Usuario(){}

    public Usuario(int id, String username, Role role, LocalDateTime createdAt) {
        this.id = id;
        this.username = username;
        this.role = role;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "--- Usuario ---  " + '\n' +
                "Id:" + id + '\n' +
                "Username:" + username + '\n' +
                "Role:" + role + '\n' +
                "CreatedAt:" + createdAt
                ;
    }
}

package ru.specialist.spring.entity;


import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name ="\"user\"")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private long userId;

    @ManyToMany
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name="role_id")
    )
    private List<Role> roles;

    private String username;

    private String password;

    @OneToMany(mappedBy = "user")
    private List<Post> posts;

    @Column(name = "is_active")
    private boolean isActive;

    @Column(name = "dt_created")
    private LocalDateTime dtCreated = LocalDateTime.now();

    public long getUserId() { return userId; }

    public void setUserId(long userId) { this.userId = userId; }

    public List<Role> getRoles() { return roles; }

    public void setRoles(List<Role> roles) { this.roles = roles; }

    public String getUsername() { return username; }

    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }

    public List<Post> getPosts() { return posts; }

    public void setPosts(List<Post> posts) { this.posts = posts; }

    public boolean getIsActive() { return isActive; }

    public void setActive(boolean active) { isActive = active; }

    public LocalDateTime getdtCreated() { return dtCreated; }

    public void setdtCreated(LocalDateTime dtCreated) { this.dtCreated = dtCreated; }
}

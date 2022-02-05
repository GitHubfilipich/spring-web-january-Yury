package ru.specialist.spring.entity;

import javax.persistence.*;
import java.util.List;

@Entity
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private long roleId;

    private String name;

    @ManyToMany(mappedBy = "roles")
    private List<User> users;

    public long getRoleId() { return roleId; }

    public void setRoleId(long roleId) { this.roleId = roleId; }

    public String getName() { return name; }

    public void setName(String name) {this.name = name;}

    public List<User> getUsers() { return users; }

    public void setUsers(List<User> users) { this.users = users;
    }
}

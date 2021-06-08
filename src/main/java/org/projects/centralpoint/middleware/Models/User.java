package org.projects.centralpoint.middleware.Models;

import javax.persistence.*;

@Entity
@Table(name = "users")
public class User
{
    @Id
    @Column(name = "user_key")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "people_key")
    private Person person;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public Person getPerson() { return person; }
    public void setPerson(Person p)
    {
        person = p;
        person.setUser(this);
    }
}

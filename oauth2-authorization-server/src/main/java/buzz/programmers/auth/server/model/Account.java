package buzz.programmers.auth.server.model;


import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Objects;

public class Account implements Comparable<Account> {
    private String email;
    @JsonIgnore
    private String password;
    private String firstName;
    private String lastName;
    private String[] roles;

    public Account() {
    }

    public Account(final String password, final String firstName,
                   final String lastName, final String email, final String[] roles) {
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.roles = roles;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String[] getRoles() {
        return roles;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRoles(String[] roles) {
        this.roles = roles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Account user = (Account) o;
        return email.equals(user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }

    @Override
    public int compareTo(final Account user) {
        return this.email.compareTo(user.email);
    }
}

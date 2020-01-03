package htwb.ai.mundt.user;

import htwb.ai.mundt.storage.Identifiable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "users")
public class User implements Identifiable<String> {
    @Id
    private String userid;
    private String key;
    private String firstname;
    private String lastname;

    @Override public String getId() {
        return this.userid;
    }

    public void setUserid(String userId) {
        this.userid = userId;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String label) {
        this.key = label;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstName) {
        this.firstname = firstName;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastName) {
        this.lastname = lastName;
    }

    @Override
    public String toString() {
        return String.format("User(%s, %s, %s, %s)", userid, key, firstname, lastname);
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User other = (User) o;

        return
                userid.equals(other.userid) &&
                key.equals(other.key) &&
                firstname.equals(other.firstname) &&
                lastname.equals(other.lastname);
    }

    @Override public int hashCode() {
        return userid.hashCode() + key.hashCode() + firstname.hashCode() + lastname.hashCode();
    }
}

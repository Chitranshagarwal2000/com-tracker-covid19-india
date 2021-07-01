package covidindiatracker.comtrackercovid19india.domain;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "USER")
public class User{

    @Id
    @Column(name = "USER_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(name = "USERNAME", nullable = false)
    private String username;

    @Column(name = "MOBILE", nullable = false, length = 13)
    private String mobileNumber;

    @Column(name = "STATE", nullable = false)
    private String state;

    @Column(name = "DISTRICT", nullable = false)
    private String district;

    public User(Long userId, String username, String mobileNumber, String state, String district) {
        this.userId = userId;
        this.username = username;
        this.mobileNumber = mobileNumber;
        this.state = state;
        this.district = district;
    }

    public User() {
    }

    public static UserBuilder builder() {
        return new UserBuilder();
    }

    public Long getUserId() {
        return this.userId;
    }

    public String getUsername() {
        return this.username;
    }

    public String getMobileNumber() {
        return this.mobileNumber;
    }

    public String getState() {
        return this.state;
    }

    public String getDistrict() {
        return this.district;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(mobileNumber, user.mobileNumber) && Objects.equals(district, user.district);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mobileNumber, district);
    }

    public String toString() {
        return "User(userId=" + this.getUserId() + ", username=" + this.getUsername() + ", mobileNumber=" + this.getMobileNumber() + ", state=" + this.getState() + ", district=" + this.getDistrict() + ")";
    }

    public static class UserBuilder {
        private Long userId;
        private String username;
        private String mobileNumber;
        private String state;
        private String district;

        UserBuilder() {
        }

        public User.UserBuilder userId(Long userId) {
            this.userId = userId;
            return this;
        }

        public User.UserBuilder username(String username) {
            this.username = username;
            return this;
        }

        public User.UserBuilder mobileNumber(String mobileNumber) {
            this.mobileNumber = mobileNumber;
            return this;
        }

        public User.UserBuilder state(String state) {
            this.state = state;
            return this;
        }

        public User.UserBuilder district(String district) {
            this.district = district;
            return this;
        }

        public User build() {
            return new User(userId, username, mobileNumber, state, district);
        }

        public String toString() {
            return "User.UserBuilder(userId=" + this.userId + ", username=" + this.username + ", mobileNumber=" + this.mobileNumber + ", state=" + this.state + ", district=" + this.district + ")";
        }
    }
}

package covidindiatracker.comtrackercovid19india.domain;


import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "STATE")
public class State {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "STATE_ID", nullable = false)
    private Long stateId;

    @Column(name = "STATE_CD")
    private String stateCode;

    @Column
    private String stateName;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "STATE_ID", nullable = false)
    private Set<District> districts;

    public State(Long stateId, String stateCode, String stateName, Set<District> districts) {
        this.stateId = stateId;
        this.stateCode = stateCode;
        this.stateName = stateName;
        this.districts = districts;
    }

    public State() {
    }

    public static StateBuilder builder() {
        return new StateBuilder();
    }

    public Long getStateId() {
        return this.stateId;
    }

    public String getStateCode() {
        return this.stateCode;
    }

    public String getStateName() {
        return this.stateName;
    }

    public Set<District> getDistricts() {
        return this.districts;
    }

    public void setStateId(Long stateId) {
        this.stateId = stateId;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public void setDistricts(Set<District> districts) {
        this.districts = districts;
    }

    public String toString() {
        return "State(stateId=" + this.getStateId() + ", stateCode=" + this.getStateCode() + ", stateName=" + this.getStateName() + ", districts=" + this.getDistricts() + ")";
    }

    public static class StateBuilder {
        private Long stateId;
        private String stateCode;
        private String stateName;
        private Set<District> districts;

        StateBuilder() {
        }

        public StateBuilder stateId(Long stateId) {
            this.stateId = stateId;
            return this;
        }

        public StateBuilder stateCode(String stateCode) {
            this.stateCode = stateCode;
            return this;
        }

        public StateBuilder stateName(String stateName) {
            this.stateName = stateName;
            return this;
        }

        public StateBuilder districts(Set<District> districts) {
            this.districts = districts;
            return this;
        }

        public State build() {
            return new State(stateId, stateCode, stateName, districts);
        }

        public String toString() {
            return "State.StateBuilder(stateId=" + this.stateId + ", stateCode=" + this.stateCode + ", stateName=" + this.stateName + ", districts=" + this.districts + ")";
        }
    }
}
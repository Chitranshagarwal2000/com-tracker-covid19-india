package covidindiatracker.comtrackercovid19india.domain;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "DISTRICT")
public class District {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DISTRICT_ID", nullable = false)
    private Long districtId;

    @Column(name = "DISTRICT_NAME")
    private String districtName;

    @Column(name = "NOTES")
    private String notes;

    @Column(name = "ACTIVE")
    private Integer active;

    @Column(name = "CONFIRMED")
    private Integer confirmed;

    @Column(name = "DECEASED")
    private Integer deceased;

    @Column(name = "RECOVERED")
    private Integer recovered;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "district")
//    @JoinColumn(name = "DISTRICT_ID", referencedColumnName = "DISTRICT_ID")
    @PrimaryKeyJoinColumn
    private Delta delta;

    @Column(name = "STATE_ID", nullable = false, updatable = false, insertable = false)
    private Long stateId;


    public District(Long districtId, String districtName, String notes, Integer active, Integer confirmed, Integer deceased, Integer recovered, Delta delta, Long stateId) {
        this.districtId = districtId;
        this.districtName = districtName;
        this.notes = notes;
        this.active = active;
        this.confirmed = confirmed;
        this.deceased = deceased;
        this.recovered = recovered;
        this.delta = delta;
        this.stateId = stateId;
    }

    public District() {
    }

    public static DistrictBuilder builder() {
        return new DistrictBuilder();
    }

    public Long getDistrictId() {
        return this.districtId;
    }

    public String getDistrictName() {
        return this.districtName;
    }

    public String getNotes() {
        return this.notes;
    }

    public Integer getActive() {
        return this.active;
    }

    public Integer getConfirmed() {
        return this.confirmed;
    }

    public Integer getDeceased() {
        return this.deceased;
    }

    public Integer getRecovered() {
        return this.recovered;
    }

    public Delta getDelta() {
        return this.delta;
    }

    public Long getStateId() {
        return this.stateId;
    }

    public void setDistrictId(Long districtId) {
        this.districtId = districtId;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setActive(Integer active) {
        this.active = active;
    }

    public void setConfirmed(Integer confirmed) {
        this.confirmed = confirmed;
    }

    public void setDeceased(Integer deceased) {
        this.deceased = deceased;
    }

    public void setRecovered(Integer recovered) {
        this.recovered = recovered;
    }

    public void setDelta(Delta delta) {
        delta.setDistrict(this);
        this.delta = delta;
    }

    public void setStateId(Long stateId) {
        this.stateId = stateId;
    }

    public String toString() {
        return "District(districtId=" + this.getDistrictId() + ", districtName=" + this.getDistrictName() + ", notes=" + this.getNotes() + ", active=" + this.getActive() + ", confirmed=" + this.getConfirmed() + ", deceased=" + this.getDeceased() + ", recovered=" + this.getRecovered() + ", delta=" + this.getDelta() + ", stateId=" + this.getStateId() + ")";
    }

    public static class DistrictBuilder {
        private Long districtId;
        private String districtName;
        private String notes;
        private Integer active;
        private Integer confirmed;
        private Integer deceased;
        private Integer recovered;
        private Delta delta;
        private Long stateId;

        DistrictBuilder() {
        }

        public DistrictBuilder districtId(Long districtId) {
            this.districtId = districtId;
            return this;
        }

        public DistrictBuilder districtName(String districtName) {
            this.districtName = districtName;
            return this;
        }

        public DistrictBuilder notes(String notes) {
            this.notes = notes;
            return this;
        }

        public DistrictBuilder active(Integer active) {
            this.active = active;
            return this;
        }

        public DistrictBuilder confirmed(Integer confirmed) {
            this.confirmed = confirmed;
            return this;
        }

        public DistrictBuilder deceased(Integer deceased) {
            this.deceased = deceased;
            return this;
        }

        public DistrictBuilder recovered(Integer recovered) {
            this.recovered = recovered;
            return this;
        }

        public DistrictBuilder delta(Delta delta) {
            this.delta = delta;
            return this;
        }

        public DistrictBuilder stateId(Long stateId) {
            this.stateId = stateId;
            return this;
        }

        public District build() {
            return new District(districtId, districtName, notes, active, confirmed, deceased, recovered, delta, stateId);
        }

        public String toString() {
            return "District.DistrictBuilder(districtId=" + this.districtId + ", districtName=" + this.districtName + ", notes=" + this.notes + ", active=" + this.active + ", confirmed=" + this.confirmed + ", deceased=" + this.deceased + ", recovered=" + this.recovered + ", delta=" + this.delta + ", stateId=" + this.stateId + ")";
        }
    }
}

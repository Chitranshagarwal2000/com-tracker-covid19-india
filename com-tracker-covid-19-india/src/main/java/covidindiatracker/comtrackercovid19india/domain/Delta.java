package covidindiatracker.comtrackercovid19india.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "DELTA")
public class Delta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DELTA_ID", nullable = false)
    private Long deltaId;

    @Column(name = "CONFIRMED")
    private Integer confirmed;

    @Column(name = "DECEASED")
    private Integer deceased;

    @Column(name = "RECOVERED")
    private Integer recovered;

    @Column(name = "DISTRICT_ID", insertable = false, updatable = false)
    private Long districtId;

    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "DISTRICT_ID", referencedColumnName = "DISTRICT_ID",nullable = false)
    private District district;


    public Delta(Long deltaId, Integer confirmed, Integer deceased, Integer recovered, Long districtId) {
        this.deltaId = deltaId;
        this.confirmed = confirmed;
        this.deceased = deceased;
        this.recovered = recovered;
        this.districtId = districtId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Delta delta = (Delta) o;
        return Objects.equals(deltaId, delta.deltaId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(deltaId);
    }

    public Delta() {
    }

    public static DeltaBuilder builder() {
        return new DeltaBuilder();
    }

    public Long getDeltaId() {
        return this.deltaId;
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

    public Long getDistrictId() {
        return this.districtId;
    }

    public void setDeltaId(Long deltaId) {
        this.deltaId = deltaId;
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

    public void setDistrictId(Long districtId) {
        this.districtId = districtId;
    }

    public String toString() {
        return "Delta(deltaId=" + this.getDeltaId() + ", confirmed=" + this.getConfirmed() + ", deceased=" + this.getDeceased() + ", recovered=" + this.getRecovered() + ", districtId=" + this.getDistrictId() + ")";
    }

    public District getDistrict() {
        return district;
    }

    public void setDistrict(District district) {
        this.district = district;
    }

    public static class DeltaBuilder {
        private Long deltaId;
        private Integer confirmed;
        private Integer deceased;
        private Integer recovered;
        private Long districtId;

        DeltaBuilder() {
        }

        public Delta.DeltaBuilder deltaId(Long deltaId) {
            this.deltaId = deltaId;
            return this;
        }

        public Delta.DeltaBuilder confirmed(Integer confirmed) {
            this.confirmed = confirmed;
            return this;
        }

        public Delta.DeltaBuilder deceased(Integer deceased) {
            this.deceased = deceased;
            return this;
        }

        public Delta.DeltaBuilder recovered(Integer recovered) {
            this.recovered = recovered;
            return this;
        }

        public Delta.DeltaBuilder districtId(Long districtId) {
            this.districtId = districtId;
            return this;
        }

        public Delta build() {
            return new Delta(deltaId, confirmed, deceased, recovered, districtId);
        }

        public String toString() {
            return "Delta.DeltaBuilder(deltaId=" + this.deltaId + ", confirmed=" + this.confirmed + ", deceased=" + this.deceased + ", recovered=" + this.recovered + ", districtId=" + this.districtId + ")";
        }
    }
}

package covidindiatracker.comtrackercovid19india.domain;

import javax.persistence.*;

@Entity
@Table(name = "DELTA")
public class Delta {

    @Id
    @Column(name = "DISTRICT_ID")
    private Long districtId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "DISTRICT_ID")
    private District district;

    @Column(name = "CONFIRMED")
    private Integer confirmed;

    @Column(name = "DECEASED")
    private Integer deceased;

    @Column(name = "RECOVERED")
    private Integer recovered;

    public Delta(Long districtId, District district, Integer confirmed, Integer deceased, Integer recovered) {
        this.districtId = districtId;
        this.district = district;
        this.confirmed = confirmed;
        this.deceased = deceased;
        this.recovered = recovered;
    }

    public Delta() {
    }

    public static DeltaBuilder builder() {
        return new DeltaBuilder();
    }

    public Long getDistrictId() {
        return this.districtId;
    }

    public District getDistrict() {
        return this.district;
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

    public void setDistrictId(Long districtId) {
        this.districtId = districtId;
    }

    public void setDistrict(District district) {
        this.district = district;
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

    @Override
    public String toString() {
        return "Delta{" +
                "districtId=" + districtId +
                ", confirmed=" + confirmed +
                ", deceased=" + deceased +
                ", recovered=" + recovered +
                '}';
    }

    public static class DeltaBuilder {
        private Long districtId;
        private District district;
        private Integer confirmed;
        private Integer deceased;
        private Integer recovered;

        DeltaBuilder() {
        }

        public Delta.DeltaBuilder districtId(Long districtId) {
            this.districtId = districtId;
            return this;
        }

        public Delta.DeltaBuilder district(District district) {
            this.district = district;
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

        public Delta build() {
            return new Delta(districtId, district, confirmed, deceased, recovered);
        }

        public String toString() {
            return "Delta.DeltaBuilder(districtId=" + this.districtId + ", district=" + this.district + ", confirmed=" + this.confirmed + ", deceased=" + this.deceased + ", recovered=" + this.recovered + ")";
        }
    }
}

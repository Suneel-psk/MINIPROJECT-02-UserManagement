package in.psk.entitys;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
@Entity
@Table(name="STATE_MASTER")
public class StateEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	private Integer stateId;
	private String stateName;
	@ManyToOne
	@JoinColumn(name="country_id")
	private CountryEntity countryEnt;
	public Integer getStateId() {
		return stateId;
	}
	public void setStateId(Integer stateId) {
		this.stateId = stateId;
	}
	public String getStateName() {
		return stateName;
	}
	public void setStateName(String stateName) {
		this.stateName = stateName;
	}
	public CountryEntity getCountryEnt() {
		return countryEnt;
	}
	public void setCountryEnt(CountryEntity countryEnt) {
		this.countryEnt = countryEnt;
	}
	@Override
	public String toString() {
		return "StateEntity [stateId=" + stateId + ", stateName=" + stateName + ", countryEnt=" + countryEnt + "]";
	}
	
	
}

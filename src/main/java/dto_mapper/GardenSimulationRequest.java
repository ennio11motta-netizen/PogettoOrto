package dto_mapper;



public class GardenSimulationRequest {

    private Integer locationId;
    private Integer giorni;

    public GardenSimulationRequest() {
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public Integer getGiorni() {
        return giorni;
    }

    public void setGiorni(Integer giorni) {
        this.giorni = giorni;
    }
}
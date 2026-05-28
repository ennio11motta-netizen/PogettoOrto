package reqResp;


public class SimulationRunRequest {

    private Integer locationId;
    private Integer plantId;
    private Integer giorni;

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public Integer getPlantId() {
        return plantId;
    }

    public void setPlantId(Integer plantId) {
        this.plantId = plantId;
    }

    public Integer getGiorni() {
        return giorni;
    }

    public void setGiorni(Integer giorni) {
        this.giorni = giorni;
    }
}
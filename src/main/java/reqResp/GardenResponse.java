package reqResp;

public class GardenResponse {

    private Integer locationId;
    private String nomeOrto;
    private Double latitudine;
    private Double longitudine;

    public GardenResponse() {
    }

    public GardenResponse(
            Integer locationId,
            String nomeOrto,
            Double latitudine,
            Double longitudine
    ) {
        this.locationId = locationId;
        this.nomeOrto = nomeOrto;
        this.latitudine = latitudine;
        this.longitudine = longitudine;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public String getNomeOrto() {
        return nomeOrto;
    }

    public void setNomeOrto(String nomeOrto) {
        this.nomeOrto = nomeOrto;
    }

    public Double getLatitudine() {
        return latitudine;
    }

    public void setLatitudine(Double latitudine) {
        this.latitudine = latitudine;
    }

    public Double getLongitudine() {
        return longitudine;
    }

    public void setLongitudine(Double longitudine) {
        this.longitudine = longitudine;
    }
}
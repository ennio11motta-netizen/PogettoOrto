package dto_mapper;



public class CreatePlantRequest {

    private Integer locationId;
    private Integer specieId;
    private String nomePianta;
    private String note;

    public CreatePlantRequest() {
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public Integer getSpecieId() {
        return specieId;
    }

    public void setSpecieId(Integer specieId) {
        this.specieId = specieId;
    }

    public String getNomePianta() {
        return nomePianta;
    }

    public void setNomePianta(String nomePianta) {
        this.nomePianta = nomePianta;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
package dto.rdf;

public class RdfPlantHistoryDTO {

    private String plantUri;
    private String name;
    private String speciesName;
    private String growthStage;
    private Double storeGDD;
    private String note;

    public RdfPlantHistoryDTO() {
    }

    public String getPlantUri() {
        return plantUri;
    }

    public void setPlantUri(String plantUri) {
        this.plantUri = plantUri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpeciesName() {
        return speciesName;
    }

    public void setSpeciesName(String speciesName) {
        this.speciesName = speciesName;
    }

    public String getGrowthStage() {
        return growthStage;
    }

    public void setGrowthStage(String growthStage) {
        this.growthStage = growthStage;
    }

    public Double getStoreGDD() {
        return storeGDD;
    }

    public void setStoreGDD(Double storeGDD) {
        this.storeGDD = storeGDD;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}

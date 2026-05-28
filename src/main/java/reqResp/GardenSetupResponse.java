package reqResp;

public class GardenSetupResponse {

    private Integer locationId;
    private String nomeOrto;

    private Integer plantId;
    private String nomePianta;

    private Integer specieId;
    private String specieNome;

    private String growthStage;
    private Double storeGDD;

    public GardenSetupResponse() {
    }

    public GardenSetupResponse(
            Integer locationId,
            String nomeOrto,
            Integer plantId,
            String nomePianta,
            Integer specieId,
            String specieNome,
            String growthStage,
            Double storeGDD
    ) {
        this.locationId = locationId;
        this.nomeOrto = nomeOrto;
        this.plantId = plantId;
        this.nomePianta = nomePianta;
        this.specieId = specieId;
        this.specieNome = specieNome;
        this.growthStage = growthStage;
        this.storeGDD = storeGDD;
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

    public Integer getPlantId() {
        return plantId;
    }

    public void setPlantId(Integer plantId) {
        this.plantId = plantId;
    }

    public String getNomePianta() {
        return nomePianta;
    }

    public void setNomePianta(String nomePianta) {
        this.nomePianta = nomePianta;
    }

    public Integer getSpecieId() {
        return specieId;
    }

    public void setSpecieId(Integer specieId) {
        this.specieId = specieId;
    }

    public String getSpecieNome() {
        return specieNome;
    }

    public void setSpecieNome(String specieNome) {
        this.specieNome = specieNome;
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
}
package dto;



import model.PlantInstance;

public class PlantDTO {

    private Integer plantId;
    private String nomePianta;

    private Integer locationId;
    private String nomeOrto;

    private Integer specieId;
    private String specieNome;

    private String growthStage;
    private Double storeGDD;
    private String note;

    public PlantDTO() {
    }

    public static PlantDTO fromEntity(PlantInstance plant) {
        PlantDTO dto = new PlantDTO();

        dto.setPlantId(plant.getPlantId());
        dto.setNomePianta(plant.getNome());

        if (plant.getLocation() != null) {
            dto.setLocationId(plant.getLocation().getLocationId());
            dto.setNomeOrto(plant.getLocation().getNome());
        }

        if (plant.getPlantSpecie() != null) {
            dto.setSpecieId(plant.getPlantSpecie().getSpecieId());
            dto.setSpecieNome(plant.getPlantSpecie().getNome());
        }

        dto.setGrowthStage(
                plant.getGrowthStage() != null
                        ? plant.getGrowthStage().name()
                        : null
        );

        dto.setStoreGDD(plant.getStoreGDD());
        dto.setNote(plant.getNote());

        return dto;
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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
package dto_mapper;


import java.util.List;

public class PlantSimulationResultDTO {

    private Integer plantId;
    private String nomePianta;
    private Integer locationId;
    private String nomeOrto;
    private List<SimulationStepDTO> risultati;

    public PlantSimulationResultDTO() {
    }

    public PlantSimulationResultDTO(
            Integer plantId,
            String nomePianta,
            Integer locationId,
            String nomeOrto,
            List<SimulationStepDTO> risultati
    ) {
        this.plantId = plantId;
        this.nomePianta = nomePianta;
        this.locationId = locationId;
        this.nomeOrto = nomeOrto;
        this.risultati = risultati;
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

    public List<SimulationStepDTO> getRisultati() {
        return risultati;
    }

    public void setRisultati(List<SimulationStepDTO> risultati) {
        this.risultati = risultati;
    }
}

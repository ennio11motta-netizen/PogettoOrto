package service;

import dto.PlantSpecieDTO;
import model.PlantInstance;
import model.PlantSpecie;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repository.PlantInstanceRepository;
import repository.PlantSpecieRepository;
import reqResp.CreatePlantSpecieRequest;

import java.util.List;

@Service
public class PlantSpecieService {

    private final PlantSpecieRepository plantSpecieRepository;


    private final PlantInstanceRepository plantInstanceRepository;


    public PlantSpecieService(PlantSpecieRepository plantSpecieRepository,
                              PlantInstanceRepository plantInstanceRepository) {
        this.plantSpecieRepository = plantSpecieRepository;
        this.plantInstanceRepository = plantInstanceRepository;
    }

    @Transactional(readOnly = true)
    public List<PlantSpecieDTO> getAllSpecies() {
        return plantSpecieRepository.findAll()
                .stream()
                .map(PlantSpecieDTO::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public PlantSpecieDTO getSpecieById(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("Id specie obbligatorio");
        }

        PlantSpecie specie = plantSpecieRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Specie non trovata con id: " + id
                ));

        return PlantSpecieDTO.fromEntity(specie);
    }

    @Transactional
    public PlantSpecieDTO createSpecie(CreatePlantSpecieRequest request) {
        validateCreateSpecieRequest(request);

        plantSpecieRepository.findByNomeIgnoreCase(request.getNome())
                .ifPresent(existing -> {
                    throw new IllegalArgumentException(
                            "Esiste già una specie con nome: " + request.getNome()
                    );
                });

        PlantSpecie specie = new PlantSpecie();
        specie.setNome(request.getNome());
        specie.setTempBase(request.getTempBase());

        specie.setGddEmergenza(request.getGddEmergenza());
        specie.setGddSviluppo(request.getGddSviluppo());
        specie.setGddFioritura(request.getGddFioritura());
        specie.setGddFruttificazione(request.getGddFruttificazione());
        specie.setGddMaturazione(request.getGddMaturazione());

        specie.setSogliaStressCaldo(request.getSogliaStressCaldo());
        specie.setSogliaStressFreddo(request.getSogliaStressFreddo());

        specie.setSensibilitaMalattie(request.getSensibilitaMalattie());
        specie.setNoteAgronomiche(request.getNoteAgronomiche());

        PlantSpecie savedSpecie = plantSpecieRepository.save(specie);

        return PlantSpecieDTO.fromEntity(savedSpecie);
    }

    private void validateCreateSpecieRequest(CreatePlantSpecieRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("La richiesta non può essere null");
        }

        if (request.getNome() == null || request.getNome().isBlank()) {
            throw new IllegalArgumentException("Il nome della specie è obbligatorio");
        }

        if (request.getTempBase() == null) {
            throw new IllegalArgumentException("La temperatura base è obbligatoria");
        }

        if (request.getGddEmergenza() == null ||
                request.getGddSviluppo() == null ||
                request.getGddFioritura() == null ||
                request.getGddFruttificazione() == null ||
                request.getGddMaturazione() == null) {
            throw new IllegalArgumentException("Le soglie GDD sono obbligatorie");
        }

        if (request.getSogliaStressCaldo() == null ||
                request.getSogliaStressFreddo() == null) {
            throw new IllegalArgumentException("Le soglie di stress caldo/freddo sono obbligatorie");
        }

        if (request.getGddEmergenza() < 0 ||
                request.getGddSviluppo() < 0 ||
                request.getGddFioritura() < 0 ||
                request.getGddFruttificazione() < 0 ||
                request.getGddMaturazione() < 0) {
            throw new IllegalArgumentException("Le soglie GDD non possono essere negative");
        }

        if (!(request.getGddEmergenza() <= request.getGddSviluppo()
                && request.getGddSviluppo() <= request.getGddFioritura()
                && request.getGddFioritura() <= request.getGddFruttificazione()
                && request.getGddFruttificazione() <= request.getGddMaturazione())) {
            throw new IllegalArgumentException(
                    "Le soglie GDD devono essere in ordine crescente"
            );
        }
    }

    @Transactional
    public void deleteSpecie(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("Id specie obbligatorio");
        }

        PlantSpecie specie = plantSpecieRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Specie non trovata con id: " + id
                ));

        List<PlantInstance> plantsUsingSpecies =
                plantInstanceRepository.findBySpecie(specie);

        if (!plantsUsingSpecies.isEmpty()) {
            throw new IllegalStateException(
                    "Impossibile eliminare la specie: esistono "
                            + plantsUsingSpecies.size()
                            + " piante associate"
            );
        }

        plantSpecieRepository.deleteById(id);
    }


}


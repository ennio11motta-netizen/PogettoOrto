package model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.Set;

@Entity
public class PlantSpecie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer specieId;

    private String nome;
    private Double tempBase;



    private Double gddEmergenza;
    private Double gddSviluppo;
    private Double gddFioritura;
    private Double gddFruttificazione;
    private Double gddMaturazione;


    private Double sogliaStressCaldo;
    private Double sogliaStressFreddo;
    private String sensibilitaMalattie;
    private String noteAgronomiche;



    //1 plantSpecies -> n plantInstances
    //private Set<PlantInstance> plantInstances;


    public PlantSpecie(Double gddEmergenza, Double gddFioritura, Double gddFruttificazione, Double gddMaturazione, Double gddSviluppo, String nome, String noteAgronomiche, Set<PlantInstance> plantInstances, String sensibilitaMalattie, Double sogliaStressCaldo, Double sogliaStressFreddo, Long specieId, Double tempBase) {
        this.gddEmergenza = gddEmergenza;
        this.gddFioritura = gddFioritura;
        this.gddFruttificazione = gddFruttificazione;
        this.gddMaturazione = gddMaturazione;
        this.gddSviluppo = gddSviluppo;
        this.nome = nome;
        this.noteAgronomiche = noteAgronomiche;
      //  this.plantInstances = plantInstances;
        this.sensibilitaMalattie = sensibilitaMalattie;
        this.sogliaStressCaldo = sogliaStressCaldo;
        this.sogliaStressFreddo = sogliaStressFreddo;
        this.tempBase = tempBase;
    }


    public PlantSpecie() {
    }

    public Double getGddEmergenza() {
        return gddEmergenza;
    }

    public void setGddEmergenza(Double gddEmergenza) {
        this.gddEmergenza = gddEmergenza;
    }

    public Double getGddFioritura() {
        return gddFioritura;
    }

    public void setGddFioritura(Double gddFioritura) {
        this.gddFioritura = gddFioritura;
    }

    public Double getGddFruttificazione() {
        return gddFruttificazione;
    }

    public void setGddFruttificazione(Double gddFruttificazione) {
        this.gddFruttificazione = gddFruttificazione;
    }

    public Double getGddMaturazione() {
        return gddMaturazione;
    }

    public void setGddMaturazione(Double gddMaturazione) {
        this.gddMaturazione = gddMaturazione;
    }

    public Double getGddSviluppo() {
        return gddSviluppo;
    }

    public void setGddSviluppo(Double gddSviluppo) {
        this.gddSviluppo = gddSviluppo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNoteAgronomiche() {
        return noteAgronomiche;
    }

    public void setNoteAgronomiche(String noteAgronomiche) {
        this.noteAgronomiche = noteAgronomiche;
    }

//    public Set<PlantInstance> getPlantInstances() {
//        return plantInstances;
//    }

//    public void setPlantInstances(Set<PlantInstance> plantInstances) {
//        this.plantInstances = plantInstances;
//    }

    public String getSensibilitaMalattie() {
        return sensibilitaMalattie;
    }

    public void setSensibilitaMalattie(String sensibilitaMalattie) {
        this.sensibilitaMalattie = sensibilitaMalattie;
    }

    public Double getSogliaStressCaldo() {
        return sogliaStressCaldo;
    }

    public void setSogliaStressCaldo(Double sogliaStressCaldo) {
        this.sogliaStressCaldo = sogliaStressCaldo;
    }

    public Double getSogliaStressFreddo() {
        return sogliaStressFreddo;
    }

    public void setSogliaStressFreddo(Double sogliaStressFreddo) {
        this.sogliaStressFreddo = sogliaStressFreddo;
    }

    public Integer getSpecieId() {
        return specieId;
    }

    public void setSpecieId(Integer specieId) {
        this.specieId = specieId;
    }

    public Double getTempBase() {
        return tempBase;
    }

    public void setTempBase(Double tempBase) {
        this.tempBase = tempBase;
    }

    @Override
    public String toString() {
        return "PlantSpecie{" +
                "gddEmergenza=" + gddEmergenza +
                ", specieId=" + specieId +
                ", nome='" + nome + '\'' +
                ", tempBase=" + tempBase +
                ", gddSviluppo=" + gddSviluppo +
                ", gddFioritura=" + gddFioritura +
                ", gddFruttificazione=" + gddFruttificazione +
                ", gddMaturazione=" + gddMaturazione +
                ", sogliaStressCaldo=" + sogliaStressCaldo +
                ", sogliaStressFreddo=" + sogliaStressFreddo +
                ", sensibilitaMalattie='" + sensibilitaMalattie + '\'' +
                ", noteAgronomiche='" + noteAgronomiche + '\'' +
                '}';
    }
}

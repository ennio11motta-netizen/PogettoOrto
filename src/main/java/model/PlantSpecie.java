package model;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "plant_specie")
public class PlantSpecie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "specie_id")
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
    @OneToMany(mappedBy = "plantSpecie")
    private Set<PlantInstance> plantInstances = new HashSet<PlantInstance>();


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

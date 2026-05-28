package reqResp;



public class CreatePlantSpecieRequest {

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

    public CreatePlantSpecieRequest() {
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Double getTempBase() {
        return tempBase;
    }

    public void setTempBase(Double tempBase) {
        this.tempBase = tempBase;
    }

    public Double getGddEmergenza() {
        return gddEmergenza;
    }

    public void setGddEmergenza(Double gddEmergenza) {
        this.gddEmergenza = gddEmergenza;
    }

    public Double getGddSviluppo() {
        return gddSviluppo;
    }

    public void setGddSviluppo(Double gddSviluppo) {
        this.gddSviluppo = gddSviluppo;
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

    public String getSensibilitaMalattie() {
        return sensibilitaMalattie;
    }

    public void setSensibilitaMalattie(String sensibilitaMalattie) {
        this.sensibilitaMalattie = sensibilitaMalattie;
    }

    public String getNoteAgronomiche() {
        return noteAgronomiche;
    }

    public void setNoteAgronomiche(String noteAgronomiche) {
        this.noteAgronomiche = noteAgronomiche;
    }
}
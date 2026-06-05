package reqResp;

public class CreateGardenRequest {

    private String nomeOrto;
    private Double latitudine;
    private Double longitudine;

    public CreateGardenRequest() {
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

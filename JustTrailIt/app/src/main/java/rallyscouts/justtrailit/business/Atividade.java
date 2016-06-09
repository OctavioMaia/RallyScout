package rallyscouts.justtrailit.business;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by rjaf on 09/06/16.
 */
public class Atividade {

    private int idAtividade;
    private Date inicioReconhecimento;
    private Date fimReconhecimento;
    private String nomeEquipa;
    private boolean inProgress;
    private Mapa percurso;
    private ArrayList<Nota> notas;
    private ArrayList<Veiculo> veiculos;

    public Atividade(Date fimReconhecimento, int idAtividade, Date inicioReconhecimento, boolean inProgress, String nomeEquipa, ArrayList<Nota> notas, Mapa percurso, ArrayList<Veiculo> veiculos) {
        this.fimReconhecimento = fimReconhecimento;
        this.idAtividade = idAtividade;
        this.inicioReconhecimento = inicioReconhecimento;
        this.inProgress = inProgress;
        this.nomeEquipa = nomeEquipa;
        this.notas = notas;
        this.percurso = percurso;
        this.veiculos = veiculos;
    }

    public Atividade(int idAtividade, boolean inProgress, String nomeEquipa, Mapa percurso, ArrayList<Veiculo> veiculos) {
        this.idAtividade = idAtividade;
        this.inProgress = inProgress;
        this.nomeEquipa = nomeEquipa;
        this.percurso = percurso;
        this.veiculos = veiculos;
    }

    public Date getFimReconhecimento() {
        return fimReconhecimento;
    }

    public void setFimReconhecimento(Date fimReconhecimento) {
        this.fimReconhecimento = fimReconhecimento;
    }

    public int getIdAtividade() {
        return idAtividade;
    }

    public void setIdAtividade(int idAtividade) {
        this.idAtividade = idAtividade;
    }

    public Date getInicioReconhecimento() {
        return inicioReconhecimento;
    }

    public void setInicioReconhecimento(Date inicioReconhecimento) {
        this.inicioReconhecimento = inicioReconhecimento;
    }

    public boolean isInProgress() {
        return inProgress;
    }

    public void setInProgress(boolean inProgress) {
        this.inProgress = inProgress;
    }

    public String getNomeEquipa() {
        return nomeEquipa;
    }

    public void setNomeEquipa(String nomeEquipa) {
        this.nomeEquipa = nomeEquipa;
    }

    public ArrayList<Nota> getNotas() {
        return notas;
    }

    public void setNotas(ArrayList<Nota> notas) {
        this.notas = notas;
    }

    public Mapa getPercurso() {
        return percurso;
    }

    public void setPercurso(Mapa percurso) {
        this.percurso = percurso;
    }

    public ArrayList<Veiculo> getVeiculos() {
        return veiculos;
    }

    public void setVeiculos(ArrayList<Veiculo> veiculos) {
        this.veiculos = veiculos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Atividade)) return false;

        Atividade atividade = (Atividade) o;

        if (idAtividade != atividade.idAtividade) return false;
        if (inProgress != atividade.inProgress) return false;
        if (inicioReconhecimento != null ? !inicioReconhecimento.equals(atividade.inicioReconhecimento) : atividade.inicioReconhecimento != null)
            return false;
        if (fimReconhecimento != null ? !fimReconhecimento.equals(atividade.fimReconhecimento) : atividade.fimReconhecimento != null)
            return false;
        if (nomeEquipa != null ? !nomeEquipa.equals(atividade.nomeEquipa) : atividade.nomeEquipa != null)
            return false;
        if (percurso != null ? !percurso.equals(atividade.percurso) : atividade.percurso != null)
            return false;
        if (notas != null ? !notas.equals(atividade.notas) : atividade.notas != null) return false;
        return !(veiculos != null ? !veiculos.equals(atividade.veiculos) : atividade.veiculos != null);

    }

    @Override
    public int hashCode() {
        return idAtividade;
    }
}

package rallyscouts.justtrailit.business;

import android.content.Context;

import java.util.ArrayList;
import java.util.Date;

import rallyscouts.justtrailit.data.MapaDAO;
import rallyscouts.justtrailit.data.NotaDAO;
import rallyscouts.justtrailit.data.VeiculoDAO;

/**
 * Created by rjaf on 09/06/16.
 */
public class Atividade {

    private int idAtividade;
    private String nomeEquipa;

    public Atividade(int idAtividade, String nomeEquipa) {
        this.idAtividade = idAtividade;
        this.nomeEquipa = nomeEquipa;
    }

    public int getIdAtividade() {
        return idAtividade;
    }

    public void setIdAtividade(int idAtividade) {
        this.idAtividade = idAtividade;
    }

    public String getNomeEquipa() {
        return nomeEquipa;
    }

    public void setNomeEquipa(String nomeEquipa) {
        this.nomeEquipa = nomeEquipa;
    }

    @Override
    public String toString() {
        return "Atividade{" +
                ", idAtividade=" + idAtividade +
                ", nomeEquipa='" + nomeEquipa + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Atividade)) return false;

        Atividade atividade = (Atividade) o;

        return idAtividade == atividade.idAtividade;

    }

    @Override
    public int hashCode() {
        return idAtividade;
    }
}

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
    private String emailEquipa;
    private MapaDAO percurso;
    private NotaDAO notas;
    private VeiculoDAO veiculos;

    public Atividade(int idAtividade, String nomeEquipa, String emailEquipa) {
        this.idAtividade = idAtividade;
        this.nomeEquipa = nomeEquipa;
        this.percurso = new MapaDAO();
        this.notas = new NotaDAO();
        this.veiculos = new VeiculoDAO();
    }
}

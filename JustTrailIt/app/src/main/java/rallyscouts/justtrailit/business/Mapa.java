package rallyscouts.justtrailit.business;

import android.location.Location;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by rjaf on 09/06/16.
 */
public class Mapa {

    private int idMapa; // id na base de dados
    private String nomeProva;
    private Map<Integer,Location> coord;


    public Mapa(Map<Integer, Location> coord, int idMapa, String nomeProva) {
        this.coord = coord;
        this.idMapa = idMapa;
        this.nomeProva = nomeProva;
    }

    public Mapa(int idMapa, String nomeProva) {
        this.idMapa = idMapa;
        this.nomeProva = nomeProva;
        this.coord = new HashMap<>();
    }

    public Mapa(int idMapa) {
        this.idMapa = idMapa;
        this.nomeProva=null;
        this.coord=null;
    }

    public Map<Integer, Location> getCoord() {
        return coord;
    }

    public void setCoord(Map<Integer, Location> coord) {
        this.coord = coord;
    }

    public int getIdMapa() {
        return idMapa;
    }

    public void setIdMapa(int idMapa) {
        this.idMapa = idMapa;
    }

    public String getNomeProva() {
        return nomeProva;
    }

    public void setNomeProva(String nomeProva) {
        this.nomeProva = nomeProva;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Mapa)) return false;

        Mapa mapa = (Mapa) o;

        if (idMapa != mapa.idMapa) return false;
        if (!nomeProva.equals(mapa.nomeProva)) return false;
        return coord != null ? coord.equals(mapa.coord) : mapa.coord == null;

    }

    @Override
    public int hashCode() {
        return idMapa;
    }

    public void addCoord(Integer ord, Location loc){
        this.coord.put(ord,loc);
    }
}

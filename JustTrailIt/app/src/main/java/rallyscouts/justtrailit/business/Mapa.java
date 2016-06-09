package rallyscouts.justtrailit.business;

import android.location.Location;

import java.util.Map;

/**
 * Created by rjaf on 09/06/16.
 */
public class Mapa {

    private String idMapa; // id na base de dados
    private String nomeProva;
    private Map<Integer,Location> coord;


    public Mapa(Map<Integer, Location> coord, String idMapa, String nomeProva) {
        this.coord = coord;
        this.idMapa = idMapa;
        this.nomeProva = nomeProva;
    }

    public Mapa(String idMapa, String nomeProva) {
        this.idMapa = idMapa;
        this.nomeProva = nomeProva;
    }

    public Map<Integer, Location> getCoord() {
        return coord;
    }

    public void setCoord(Map<Integer, Location> coord) {
        this.coord = coord;
    }

    public String getIdMapa() {
        return idMapa;
    }

    public void setIdMapa(String idMapa) {
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

        if (!idMapa.equals(mapa.idMapa)) return false;
        if (nomeProva != null ? !nomeProva.equals(mapa.nomeProva) : mapa.nomeProva != null)
            return false;
        return !(coord != null ? !coord.equals(mapa.coord) : mapa.coord != null);

    }

    @Override
    public int hashCode() {
        return idMapa.hashCode();
    }

    public void addCoord(Integer ord, Location loc){
        this.coord.put(ord,loc);
    }
}

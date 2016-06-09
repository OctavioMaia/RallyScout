package rallyscouts.justtrailit.business;

import java.util.ArrayList;

/**
 * Created by rjaf on 09/06/16.
 */
public class Veiculo {

    private String chassi; // id do veiculo
    private String modelo;
    private String marca;
    private ArrayList<String> caracteristicas;

    public Veiculo(String chassi, String marca, String modelo , ArrayList<String> caracteristicas) {
        this.caracteristicas = caracteristicas;
        this.chassi = chassi;
        this.marca = marca;
        this.modelo = modelo;
    }

    public Veiculo(String chassi, String marca, String modelo) {
        this.chassi = chassi;
        this.marca = marca;
        this.modelo = modelo;
    }

    public ArrayList<String> getCaracteristicas() {
        return caracteristicas;
    }

    public void setCaracteristicas(ArrayList<String> caracteristicas) {
        this.caracteristicas = caracteristicas;
    }

    public String getChassi() {
        return chassi;
    }

    public void setChassi(String chassi) {
        this.chassi = chassi;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    @Override
    public String toString() {
        return "Veiculo{" +
                "modelo='" + modelo + '\'' +
                ", marca='" + marca + '\'' +
                ", chassi='" + chassi + '\'' +
                ", caracteristicas=" + caracteristicas +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Veiculo)) return false;

        Veiculo veiculo = (Veiculo) o;

        if (!chassi.equals(veiculo.chassi)) return false;
        if (modelo != null ? !modelo.equals(veiculo.modelo) : veiculo.modelo != null) return false;
        if (marca != null ? !marca.equals(veiculo.marca) : veiculo.marca != null) return false;
        return !(caracteristicas != null ? !caracteristicas.equals(veiculo.caracteristicas) : veiculo.caracteristicas != null);

    }

    @Override
    public int hashCode() {
        return chassi.hashCode();
    }

    public void addCaract(String text){
        this.caracteristicas.add(text);
    }
}


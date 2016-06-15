package rallyscouts.justtrailit.business;

import android.graphics.Bitmap;
import android.location.Location;
import android.media.Image;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by rjaf on 09/06/16.
 */
public class Nota implements Serializable{

    private int idNota;
    private String notaTextual;
    private Location localRegisto;
    private ArrayList<Bitmap> imagens;
    private byte[] voice;

    public Nota(Integer idNota, Location localRegisto) {
        this.idNota = idNota;
        this.localRegisto = localRegisto;
        this.notaTextual="";
        this.imagens = new ArrayList<>();
    }

    public Nota(Integer idNota, ArrayList<Bitmap> imagens, Location localRegisto, String notaTextual, byte[] voice) {
        this.idNota = idNota;
        this.imagens = imagens;
        this.localRegisto = localRegisto;
        this.notaTextual = notaTextual;
        this.voice = voice;
    }

    public int getIdNota() {
        return idNota;
    }

    public void setIdNota(int idNota) {
        this.idNota = idNota;
    }

    public ArrayList<Bitmap> getImagens() {
        return imagens;
    }

    public void setImagens(ArrayList<Bitmap> imagens) {
        this.imagens = imagens;
    }

    public Location getLocalRegisto() {
        return localRegisto;
    }

    public void setLocalRegisto(Location localRegisto) {
        this.localRegisto = localRegisto;
    }

    public String getNotaTextual() {
        return notaTextual;
    }

    public void setNotaTextual(String notaTextual) {
        this.notaTextual = notaTextual;
    }

    public byte[] getVoice() {
        return voice;
    }

    public void setVoice(byte[] voice) {
        this.voice = voice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Nota)) return false;

        Nota nota = (Nota) o;

        if (idNota!=nota.idNota) return false;
        if (notaTextual != null ? !notaTextual.equals(nota.notaTextual) : nota.notaTextual != null)
            return false;
        if (!localRegisto.equals(nota.localRegisto)) return false;
        if (imagens != null ? !imagens.equals(nota.imagens) : nota.imagens != null) return false;
        return Arrays.equals(voice, nota.voice);

    }

    @Override
    public int hashCode() {
        int result = idNota;
        result = 31 * result + localRegisto.hashCode();
        return result;
    }

}

package rallyscouts.justtrailit.business;

import android.content.Context;
import android.graphics.Bitmap;
import android.location.Location;
import android.util.Base64;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPOutputStream;

import rallyscouts.justtrailit.data.AtividadeDAO;
import rallyscouts.justtrailit.data.BatedorDAO;
import rallyscouts.justtrailit.data.MapaDAO;
import rallyscouts.justtrailit.data.VeiculoDAO;

/**
 * Created by rjaf on 13/06/16.
 */
public class JsonRC {

    public static final String TAG = "JsonClass";

    private static final String EMAIL = "email";
    private static final String PASSWORD = "password";
    private static final String ATIVIDADE = "idAtividade";
    private static final String NOME_EQUIPA = "nomeEquipa";
    private static final String MAPA = "mapa";
    private static final String NOME_PROVA = "nomeProva";
    private static final String VEICULOS = "veiculos";
    private static final String PERCURSO = "percurso";
    private static final String NOTAS = "notas";
    private static final String ID_NOTA = "idNota";
    private static final String NOTA_TEXTUAL = "notaTextual";
    private static final String LOCAL = "local";
    private static final String IMAGEM = "imagem";
    private static final String AUDIO = "audio";
    private static final String LATITUDE = "lat";
    private static final String LONGITUDE = "log";
    private static final String CHASSI = "chassi";
    private static final String MARCA = "marca";
    private static final String MODELO = "modelo";
    private static final String CARACTERISTICAS = "caracteristicas";

    public static int reciveACK(String jsonString){
        int idAtividade = -1;
        try {
            JSONObject recive = new JSONObject(jsonString);
            Log.i(TAG,"ACK: " + recive.toString());
            idAtividade = recive.getInt(ATIVIDADE);
        } catch (JSONException e) {
            Log.w(TAG,"Erro ao receber o Json ACK");
        }
        return idAtividade;
    }

    public static JSONObject downloadAtividade(String emailBatedor, String password){

        JSONObject download = new JSONObject();
        try {
            download.put(EMAIL,emailBatedor);
            download.put(PASSWORD,password);
            Log.i(TAG,download.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return download;
    }

    /**
     * Ler um json que traz toda a infromação de uma atividade para a iniciar
     * @param jsonString
     */
    public static String reciveAtividade(Context mContext, String jsonString, Batedor batedorLogin) {
        BatedorDAO batedores = new BatedorDAO(mContext);
        AtividadeDAO atividades = new AtividadeDAO(mContext);
        MapaDAO mapa = new MapaDAO(mContext);
        VeiculoDAO veiculos = new VeiculoDAO(mContext);

        String ret = "Não tem atividade disponivel";

        try {
            JSONObject recive = new JSONObject(jsonString);
            Log.i(TAG,recive.toString(4));
            int idAtividade = recive.getInt(ATIVIDADE);
            Log.i(TAG,"Atividade: " + idAtividade);
            if (idAtividade >= 0) {
                //não é codigo de erro
                batedorLogin.setAtividade(idAtividade);
                batedores.updateBatedor(batedorLogin);

                atividades.insertAtividade(idAtividade, recive.getString(NOME_EQUIPA));

                try {
                    //ler mapa agora
                    JSONObject mapaJson = recive.getJSONObject(MAPA);
                    Mapa mapaRead = readMapa(idAtividade,mapaJson);
                    mapa.insertMapa(mapaRead);

                } catch (JSONException e) {
                    Log.w(TAG, "Não foi possivel ler a key mapa do Json recebido");
                }

                try {
                    //ler veiculos agora agora
                    JSONArray veiculosJson = recive.getJSONArray(VEICULOS);
                    List<Veiculo> veiculosRead = readVeiculos(veiculosJson);
                    veiculos.insertVeiculos(idAtividade,veiculosRead);
                } catch (JSONException e) {
                    Log.w(TAG, "Não foi possivel ler a key veiculos do Json recebido");
                }

                ret = "Atividade recevida com exito";
            }
            }catch(JSONException e){
                Log.e(TAG, "Json cant reader");
            }finally{
                batedores.close();
                atividades.close();
                mapa.close();
                veiculos.close();
            }
        return ret;
    }

    public static JSONObject sendAtividade(Batedor batedorLogin, List<Nota> notas){
        JSONObject send = new JSONObject();

        try {
            send.put(ATIVIDADE,batedorLogin.getAtividade());
            send.put(EMAIL,batedorLogin.getEmail());

            JSONArray listaNotas = new JSONArray();
            for ( Nota n : notas) {
                listaNotas.put(createNota(n));
            }

            send.put(NOTAS,listaNotas);

        } catch (JSONException e) {
            Log.e(TAG,"Não foi possivel criar o Json para envio da atividade " + batedorLogin.getAtividade());
            send = null;
        }
        return send;
    }


    private static Mapa readMapa(int idAtividade, JSONObject mapaJson){
        Mapa map = new Mapa(idAtividade);

        try {
            map.setNomeProva(mapaJson.getString(NOME_PROVA));
        } catch (JSONException e) {
            Log.w(TAG, "Não foi possivel ler a key nomeProva do Json recebido");
        }

        try {
            JSONArray percurso = mapaJson.getJSONArray(PERCURSO);
            Map<Integer,Location> coords = new HashMap<>();
            for (int i = 0; i < percurso.length(); i++) {
                try{
                    JSONObject coord =  percurso.getJSONObject(i);

                    Location loc = new Location("");
                    Log.i(TAG,"Pos: " + i + " lat: " + coord.getDouble(LATITUDE) + " log: " + coord.getDouble(LONGITUDE));
                    loc.setLatitude(coord.getDouble(LATITUDE));
                    loc.setLongitude(coord.getDouble(LONGITUDE));

                    coords.put(i,loc);
                }catch (JSONException e){
                    Log.w(TAG, "Não foi possivel ler a coordenada nr " + i);
                }
            }
            map.setCoord(coords);

        } catch (JSONException e) {
            Log.w(TAG, "Não foi possivel ler a key nomeProva do Json recebido");
        }
        return map;
    }


    private static List<Veiculo> readVeiculos(JSONArray veiculosJson){
        List<Veiculo> veiculos = new ArrayList<>();

        for (int i = 0; i < veiculosJson.length(); i++) {
            try {
                Veiculo vec = new Veiculo();

                JSONObject veiculo = veiculosJson.getJSONObject(i);

                try {
                    vec.setChassi(veiculo.getString(CHASSI));
                } catch (JSONException e) {
                    Log.w(TAG, "Não foi possivel ler o chassi do veiculo na posição " + i);
                }

                try {
                    vec.setMarca(veiculo.getString(MARCA));
                } catch (JSONException e) {
                    Log.w(TAG, "Não foi possivel ler o marca do veiculo na posição " + i);
                }

                try {
                    vec.setModelo(veiculo.getString(MODELO));
                } catch (JSONException e) {
                    Log.w(TAG, "Não foi possivel ler o modelo do veiculo na posição " + i);
                }

                try {
                    JSONArray caract = veiculo.getJSONArray(CARACTERISTICAS);
                    Log.w(TAG, "Número de caracteristicas " + caract.length() + " - " + vec.getChassi());
                    for (int j = 0; j < caract.length(); j++) {
                        Log.w(TAG, "Caract: " + caract.getString(j));
                        vec.addCaract(caract.getString(j));
                    }
                } catch (JSONException e) {
                    Log.w(TAG, "Não foi possivel ler as caracteristicas do veiculo " + i);
                }
                veiculos.add(vec);
            } catch (JSONException e) {
                Log.w(TAG, "Não foi possivel ler o veiculo na posição " + i);
            }
        }
        return veiculos;
    }

    private static JSONObject createNota(Nota nota){
        JSONObject notaJson = new JSONObject();

        try {
            notaJson.put(ID_NOTA,nota.getIdNota());
            notaJson.put(NOTA_TEXTUAL,nota.getNotaTextual());
            notaJson.put(LOCAL,createLocal(nota.getLocalRegisto()));
            if(nota.getImagens().size()>0){
               notaJson.put(IMAGEM, createImagens(nota.getImagens()));
            }
            if(nota.getVoice()!=null){
                notaJson.put(AUDIO, Base64.encodeToString(nota.getVoice(),Base64.DEFAULT) );
            }
        } catch (JSONException e) {
            Log.e(TAG,"Não foi possivel criar o Json para a nota " + nota.getIdNota());
            notaJson = null;
        }
        return notaJson;
    }

    private static JSONObject createLocal(Location loc){
        JSONObject localJson = new JSONObject();

        try {
            localJson.put(LATITUDE,loc.getLatitude());
            localJson.put(LONGITUDE,loc.getLongitude());
        } catch (JSONException e) {
            Log.w(TAG, "Não foi possivel criar o local de registo");
            localJson = null;
        }
        return localJson;
    }

    private static JSONArray createImagens(List<Bitmap> listaImagens){
        JSONArray imagensJson = new JSONArray();

        for (Bitmap bitmap : listaImagens ) {
            ByteBuffer byteBuffer = ByteBuffer.allocate(bitmap.getRowBytes() * bitmap.getHeight());
            bitmap.copyPixelsToBuffer(byteBuffer);
                //ByteArrayOutputStream rstBao = new ByteArrayOutputStream();
                //GZIPOutputStream zos = new GZIPOutputStream(rstBao);
                //zos.write(byteBuffer.array());
            Log.i(TAG,"size: " + byteBuffer.array().length);

            byte[] aux = byteBuffer.array();

            JSONArray imagem = new JSONArray();
            for(int i=0;i<aux.length;i++){
                imagem.put((int)aux[i]);
            }

            imagensJson.put(imagem);
        }

        return imagensJson;

    }

}

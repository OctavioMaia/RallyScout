package rallyscouts.justtrailit.business;

import android.content.Context;
import android.graphics.Bitmap;
import android.location.Location;
import android.util.Base64;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rallyscouts.justtrailit.data.AtividadeDAO;
import rallyscouts.justtrailit.data.BatedorDAO;
import rallyscouts.justtrailit.data.MapaDAO;
import rallyscouts.justtrailit.data.VeiculoDAO;

/**
 * Created by rjaf on 13/06/16.
 */
public class JsonRC {

    public static final String TAG = "JsonClass";

    public static JSONObject downloadAtividade(String emailBatedor, String password){

        JSONObject download = new JSONObject();
        try {
            download.put("email",emailBatedor);
            download.put("password",password);
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
            int idAtividade = recive.getInt("idAtividade");
            Log.i(TAG,"Atividade: " + idAtividade);
            if (idAtividade >= 0) {
                //não é codigo de erro
                batedorLogin.setAtividade(idAtividade);
                batedores.updateBatedor(batedorLogin);

                atividades.insertAtividade(idAtividade, recive.getString("nomeEquipa"));

                try {
                    //ler mapa agora
                    JSONObject mapaJson = recive.getJSONObject("mapa");
                    Mapa mapaRead = readMapa(idAtividade,mapaJson);
                    mapa.insertMapa(mapaRead);

                } catch (JSONException e) {
                    Log.w(TAG, "Não foi possivel ler a key mapa do Json recebido");
                }

                try {
                    //ler veiculos agora agora
                    JSONArray veiculosJson = recive.getJSONArray("veiculos");
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
            send.put("idAtividade",batedorLogin.getAtividade());
            send.put("email",batedorLogin.getEmail());

            JSONArray listaNotas = new JSONArray();
            for ( Nota n : notas) {
                listaNotas.put(createNota(n));
            }

            send.put("notas",listaNotas);

        } catch (JSONException e) {
            Log.e(TAG,"Não foi possivel criar o Json para envio da atividade " + batedorLogin.getAtividade());
            send = null;
        }
        return send;
    }


    private static Mapa readMapa(int idAtividade, JSONObject mapaJson){
        Mapa map = new Mapa(idAtividade);

        try {
            map.setNomeProva(mapaJson.getString("nomeProva"));
        } catch (JSONException e) {
            Log.w(TAG, "Não foi possivel ler a key nomeProva do Json recebido");
        }

        try {
            JSONArray percurso = mapaJson.getJSONArray("percurso");
            Map<Integer,Location> coords = new HashMap<>();
            for (int i = 0; i < percurso.length(); i++) {
                try{
                    JSONObject coord =  percurso.getJSONObject(i);

                    Location loc = new Location("");
                    loc.setLatitude(coord.getLong("lat"));
                    loc.setLongitude(coord.getLong("log"));

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
                    vec.setChassi(veiculo.getString("chassi"));
                } catch (JSONException e) {
                    Log.w(TAG, "Não foi possivel ler o chassi do veiculo na posição " + i);
                }

                try {
                    vec.setMarca(veiculo.getString("marca"));
                } catch (JSONException e) {
                    Log.w(TAG, "Não foi possivel ler o marca do veiculo na posição " + i);
                }

                try {
                    vec.setModelo(veiculo.getString("modelo"));
                } catch (JSONException e) {
                    Log.w(TAG, "Não foi possivel ler o modelo do veiculo na posição " + i);
                }

                try {
                    JSONArray caract = veiculo.getJSONArray("caracteristicas");
                    for (int j = 0; j < caract.length(); j++) {
                        vec.addCaract(caract.getString(j));
                    }
                } catch (JSONException e) {
                    Log.w(TAG, "Não foi possivel ler as caracteristicas do veiculo na posição " + i);
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
            notaJson.put("idNota",nota.getIdNota());
            notaJson.put("notaTextual",nota.getNotaTextual());
            notaJson.put("local",createLocal(nota.getLocalRegisto()));
            notaJson.put("imagem", createImagens(nota.getImagens()));
            notaJson.put("audio", Base64.encodeToString(nota.getVoice(),Base64.DEFAULT) );
        } catch (JSONException e) {
            Log.e(TAG,"Não foi possivel criar o Json para a nota " + nota.getIdNota());
            notaJson = null;
        }
        return notaJson;
    }

    private static JSONObject createLocal(Location loc){
        JSONObject localJson = new JSONObject();

        try {
            localJson.put("lat",loc.getLatitude());
            localJson.put("log",loc.getLongitude());
        } catch (JSONException e) {
            Log.w(TAG, "Não foi possivel criar o local de registo");
            return localJson = null;
        }
        return localJson;
    }

    private static JSONArray createImagens(List<Bitmap> listaImagens){
        JSONArray imagensJson = new JSONArray();

        for (Bitmap bitmap : listaImagens ) {
            ByteBuffer byteBuffer = ByteBuffer.allocate(bitmap.getRowBytes() * bitmap.getHeight());
            bitmap.copyPixelsToBuffer(byteBuffer);
            imagensJson.put(Base64.encodeToString(byteBuffer.array(),Base64.DEFAULT));
        }

        return imagensJson;

    }


}

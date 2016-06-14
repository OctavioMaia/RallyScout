package rallyscouts.justtrailit.business;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

    public static JSONObject downloadAtividade(String emailBatedor){

        JSONObject download = new JSONObject();
        try {
            download.put("email",emailBatedor);
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




}

package rallyscouts.justtrailit.business;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import rallyscouts.justtrailit.data.AtividadeDAO;
import rallyscouts.justtrailit.data.BatedorDAO;
import rallyscouts.justtrailit.data.MapaDAO;
import rallyscouts.justtrailit.data.VeiculoDAO;

/**
 * Created by rjaf on 13/06/16.
 */
public class JsonCreat {

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
    public static void reciveAtividade(Context mContext, String jsonString, Batedor batedorLogin){
        /*BatedorDAO batedores = new BatedorDAO(mContext);
        AtividadeDAO atividades = new AtividadeDAO(mContext);
        MapaDAO mapa = new MapaDAO(mContext);
        VeiculoDAO veiculos = new VeiculoDAO(mContext);

        try {
            JSONObject recive = new JSONObject(jsonString);
            Iterator<String> itKeys = recive.keys();
            while (itKeys.hasNext()){
                String key = itKeys.next();
                switch (key){
                    case "idAtividade" : {
                        batedores.updateBatedor(
                                batedorLogin.getEmail(),
                                batedorLogin.getPassword(),
                                batedorLogin.getNome(),
                                recive.getInt("idAtividade"));
                        batedorLogin.setAtividade(recive.getInt("idAtividade"));
                    }
                    case "mapa" : {
                        JSONObject mapaJson = recive.getJSONObject("mapa");

                        String nomeProva = mapaJson.get("nomeProva");

                        mapa.insertMapa();

                        }
                    }
                    case "veiculos" : {

                    }
                }
            }

        } catch (JSONException e) {
            Log.e(TAG,"Json cant reader");

        } finally {
            batedores.close();
            atividades.close();
            mapa.close();
            veiculos.close();
        }*/
    }


}

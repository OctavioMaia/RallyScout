package rallyscouts.justtrailit.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;

import rallyscouts.justtrailit.R;
import rallyscouts.justtrailit.business.Nota;

public class NotaDetails extends AppCompatActivity {

    private Nota nota;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nota_details);

        this.nota = (Nota) getIntent().getExtras().getSerializable("nota");

        TextView notaTextual = (TextView) findViewById(R.id.textView_NotaTextual);
        TableLayout tl = (TableLayout) findViewById(R.id.imagensLayout);

        notaTextual.setText(nota.getNotaTextual());

        tl.removeAllViews();

        LayoutInflater inflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        for (Bitmap bitmap : nota.getImagens() ) {
            ImageView iv = (ImageView) inflater.inflate(
                    R.layout.item_image, null);

            iv.setImageBitmap(bitmap);

            tl.addView(iv);
        }






    }
}

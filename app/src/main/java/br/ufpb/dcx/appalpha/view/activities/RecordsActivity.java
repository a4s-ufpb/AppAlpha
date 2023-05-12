package br.ufpb.dcx.appalpha.view.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import br.ufpb.dcx.appalpha.R;
import br.ufpb.dcx.appalpha.control.service.RecordsSqlService;
import br.ufpb.dcx.appalpha.model.bean.Record;

import java.util.ArrayList;

/**
 * Activity of record screen
 */
public class RecordsActivity extends AppCompatActivity {
    RecordsSqlService recordsSqlService;

    TextView firstName;
    TextView secondName;
    TextView thirdName;
    TextView fourthName;
    TextView fifthName;

    TextView firstPlace;
    TextView secondPlace;
    TextView thirdPlace;
    TextView fourthPlace;
    TextView fifthPlace;

    /**
     * On create activity, setup local variables
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records);

        loadTextViews();

        recordsSqlService = RecordsSqlService.getInstance(getApplicationContext());

        populateData();
    }

    /**
     * Populate the information in the view
     */
    public void populateData() {
        ArrayList<Record> records = recordsSqlService.getAllRecords();

        for(int i = 0; i < records.size(); i++) {

            switch (i) {

                case 0:
                    setDataToTextView(firstName, records.get(i).getName());
                    setDataToTextView(firstPlace, ""+ records.get(i).getPoints());
                    break;

                case 1:
                    setDataToTextView(secondName, records.get(i).getName());
                    setDataToTextView(secondPlace, ""+ records.get(i).getPoints());
                    break;

                case 2:
                    setDataToTextView(thirdName, records.get(i).getName());
                    setDataToTextView(thirdPlace, ""+ records.get(i).getPoints());
                    break;

                case 3:
                    setDataToTextView(fourthName, records.get(i).getName());
                    setDataToTextView(fourthPlace, ""+ records.get(i).getPoints());
                    break;

                case 4:
                    setDataToTextView(fifthName, records.get(i).getName());
                    setDataToTextView(fifthPlace, ""+ records.get(i).getPoints());
                    break;
            }

        }
    }

    /**
     * Action to go back
     *
     * @param v
     */
    public void backToMenu(View v) {
        finish();
    }

    /**
     * Set name to the text view
     * @param txt
     * @param dado
     */
    public void setDataToTextView(TextView txt, String dado) {
        txt.setText(dado);
    }

    /**
     * Setup local variables from the view
     */
    public void loadTextViews() {
        firstName = findViewById(R.id.nome_um);
        secondName = findViewById(R.id.nome_dois);
        thirdName = findViewById(R.id.nome_tres);
        fourthName = findViewById(R.id.nome_quatro);
        fifthName = findViewById(R.id.nome_cinco);

        firstPlace = findViewById(R.id.pontuacao_um);
        secondPlace = findViewById(R.id.pontuacao_dois);
        thirdPlace = findViewById(R.id.pontuacao_tres);
        fourthPlace = findViewById(R.id.pontuacao_quatro);
        fifthPlace = findViewById(R.id.pontuacao_cinco);
    }
}

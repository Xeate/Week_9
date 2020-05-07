package com.test.android.w9;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    String s1[];

    private Spinner spinner1, spinner2;

    private List<String> spinner1list = new ArrayList<String>();

    private List<String> recyclerlist = new ArrayList<String>();
    private List<String> timelist = new ArrayList<String>();

    String pvm;

    String current_date = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(new Date());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        addItemsOnSpinner1();
        addListenerOnSpinnerItemSelection();
    }

    // Lisätään spinneriin (1) sisältöä
    public void addItemsOnSpinner1() {

        Teatterit teatterit = Teatterit.getInstance();

        spinner1 = (Spinner) findViewById(R.id.maa);

        spinner1list.clear();

        for (int i = 0 ; i<teatterit.getTheaterAmount() ; i++) {
            spinner1list.add(teatterit.setTheaterList(i));
        }

         ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                 android.R.layout.simple_spinner_item, spinner1list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(dataAdapter);

    }

    // Lisätään RecyclerView:iin sisältöä spinner (1) perusteella
    public void addItemsOnRecycler(View view) {

        EditText Elokuva = (EditText)findViewById(R.id.elokuva);
        TextView info = (TextView)findViewById(R.id.textViewInfo);

        if (Elokuva.getText().toString().equals("")) {

            EditText PVM = (EditText) findViewById(R.id.pvm);

            pvm = PVM.getText().toString();

            // Tarkistetaan onko käyttäjä syöttänyt päivämäärän
            Date dateTest = null;
            try {
                dateTest = new SimpleDateFormat("dd.MM.yyyy").parse(pvm); // Pakollinen formaatti.
            } catch (Exception ex) {
                pvm = current_date;
            }

            EditText aika1 = (EditText) findViewById(R.id.aika1);
            EditText aika2 = (EditText) findViewById(R.id.aika2);

            String after = aika1.getText().toString();
            String before = aika2.getText().toString();

            /* Tarkistetaan onko käyttäjä syöttänyt aikoja. */
            Date jalkeen = null;
            Date ennen = null;
            DateFormat hourmin = new SimpleDateFormat("hh:mm");
            try {
                jalkeen = hourmin.parse(after);
                ennen = hourmin.parse(before);
            } catch (Exception ex) {
                after = "00:00";
                before = "23:59";
            }


            Teatterit teatterit = Teatterit.getInstance();

            String valinta_Teatteri = String.valueOf(spinner1.getSelectedItem());

            recyclerlist.clear();
            timelist.clear();

            String id = teatterit.getTheaterId(valinta_Teatteri);

            if (valinta_Teatteri.equals("Kaikki")) {
                recyclerlist = teatterit.readShowXML("1014", pvm, after, before);
                recyclerlist = teatterit.readShowXML("1015", pvm, after, before);
                recyclerlist = teatterit.readShowXML("1016", pvm, after, before);
                recyclerlist = teatterit.readShowXML("1017", pvm, after, before);
                recyclerlist = teatterit.readShowXML("1041", pvm, after, before);
                recyclerlist = teatterit.readShowXML("1018", pvm, after, before);
                recyclerlist = teatterit.readShowXML("1019", pvm, after, before);
                recyclerlist = teatterit.readShowXML("1021", pvm, after, before);
                recyclerlist = teatterit.readShowXML("1022", pvm, after, before);
            } else {

                recyclerlist = teatterit.readShowXML(id, pvm, after, before);
            }

            recyclerView = findViewById(R.id.recyclerView);
            timelist = teatterit.getTimeList();
            RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(this, recyclerlist, timelist);

            recyclerView.setAdapter(recyclerViewAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));

            info.setText("Näytökset:");

        } else {

            EditText PVM = (EditText) findViewById(R.id.pvm);

            pvm = PVM.getText().toString();

            // Tarkistetaan onko käyttäjä syöttänyt päivämäärän
            Date dateTest = null;
            try {
                dateTest = new SimpleDateFormat("dd.MM.yyyy").parse(pvm); // Pakollinen formaatti.
            } catch (Exception ex) {
                pvm = current_date;
            }

            EditText aika1 = (EditText) findViewById(R.id.aika1);
            EditText aika2 = (EditText) findViewById(R.id.aika2);

            String after = aika1.getText().toString();
            String before = aika2.getText().toString();

            /* Tarkistetaan onko käyttäjä syöttänyt aikoja. */
            Date jalkeen = null;
            Date ennen = null;
            DateFormat hourmin = new SimpleDateFormat("hh:mm");
            try {
                jalkeen = hourmin.parse(after);
                ennen = hourmin.parse(before);
            } catch (Exception ex) {
                after = "00:00";
                before = "23:59";
            }


            Teatterit teatterit = Teatterit.getInstance();

            String valinta_Teatteri = String.valueOf(spinner1.getSelectedItem());

            recyclerlist.clear();
            timelist.clear();

            String id = teatterit.getTheaterId(valinta_Teatteri);

            String movieName = Elokuva.getText().toString();

            //Jos valinta on kaikki teatterit
            if (valinta_Teatteri.equals("Kaikki") || valinta_Teatteri.equals("Valitse alue/teatteri")) {
                recyclerlist = teatterit.readShowXMLMovies("1014", pvm, after, before, movieName);
                recyclerlist = teatterit.readShowXMLMovies("1015", pvm, after, before, movieName);
                recyclerlist = teatterit.readShowXMLMovies("1016", pvm, after, before, movieName);
                recyclerlist = teatterit.readShowXMLMovies("1017", pvm, after, before, movieName);
                recyclerlist = teatterit.readShowXMLMovies("1041", pvm, after, before, movieName);
                recyclerlist = teatterit.readShowXMLMovies("1018", pvm, after, before, movieName);
                recyclerlist = teatterit.readShowXMLMovies("1019", pvm, after, before, movieName);
                recyclerlist = teatterit.readShowXMLMovies("1021", pvm, after, before, movieName);
                recyclerlist = teatterit.readShowXMLMovies("1022", pvm, after, before, movieName);
            } else {
                recyclerlist = teatterit.readShowXMLMovies(id, pvm, after, before, movieName);
            }

            timelist = teatterit.getTimeList();
            recyclerView = findViewById(R.id.recyclerView);
            RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(this, recyclerlist, timelist);

            recyclerView.setAdapter(recyclerViewAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));

            info.setText("Teatterit elokuvalle: " + movieName);

        }

    }



    public void onCLick(View view) {
        System.out.println("############## " + current_date);
    }

    public void addListenerOnSpinnerItemSelection() {
        spinner1 = (Spinner) findViewById(R.id.maa);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

 //               addItemsOnRecycler(String.valueOf(spinner1.getSelectedItem()), pvm);

                /*
                if (valinta_maa.equals("Kaikki")) {
                    readAllXML();
                } else if (valinta_maa.equals("Suomi")) {
                    readSuomiXML();
                } else if (valinta_maa.equals("Viro")) {
                    readViroXML();
                } else {
                    spinner2list.clear();
                    spinner2list.add("Valitse automaatti");
                }

                 */
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

}

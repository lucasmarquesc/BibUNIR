package com.unir.bib_unir.ui;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.unir.bib_unir.R;
import com.unir.bib_unir.database.LivroDAO;

import java.util.ArrayList;
import java.util.List;

public class PesquisaActivity extends AppCompatActivity {

    private TableLayout tabela;
    private LivroDAO livroDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pesquisa);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tabela = findViewById(R.id.tableLayout);
        Button btn_voltar = findViewById(R.id.btnVoltarPesquisar);

        livroDAO = new LivroDAO(this);
        Intent intent = getIntent();
        int tipo = intent.getIntExtra("tipo", 0);
        String busca = intent.getStringExtra("busca");
        List<ContentValues> lista = new ArrayList<>();

        // Decide qual método de pesquisa chamar baseado no RadioButton selecionado
        if (tipo == R.id.rbTitulo){
            lista = livroDAO.pesquisarPorTitulo(busca);
        } else if (tipo == R.id.rbAno && !busca.isEmpty()){
            lista = livroDAO.pesquisarPorAno(Integer.parseInt(busca));
        } else {
            // Default para 'Todos' ou se a busca por ano estiver vazia
            lista = livroDAO.pesquisarPorTodos();
        }

        // Popula a tabela dinamicamente
        if (lista != null){
            for (ContentValues cv : lista){
                TableRow tr = new TableRow(this);
                // Cria as células da linha
                tr.addView(criarCelula(String.valueOf(cv.getAsInteger("id"))));
                tr.addView(criarCelula(cv.getAsString("titulo")));
                tr.addView(criarCelula(cv.getAsString("autor")));
                tr.addView(criarCelula(String.valueOf(cv.getAsInteger("ano"))));
                tabela.addView(tr);
            }
        }

        btn_voltar.setOnClickListener(view -> finish());
    }

    // Método auxiliar para criar TextViews para a tabela
    private TextView criarCelula(String texto) {
        TextView tv = new TextView(this);
        tv.setText(texto);
        tv.setPadding(8, 8, 8, 8);
        return tv;
    }
}
package com.unir.bib_unir.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.unir.bib_unir.R;
import com.unir.bib_unir.database.LivroDAO;

public class MainActivity extends AppCompatActivity {

    private Button btnPesquisar, btnCadastrar, btnRemover, btnAtualizar;
    private RadioGroup rd_PesquisarPor;
    private TextInputEditText edtiPesquisar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        btnCadastrar = findViewById(R.id.btn_cadastrar);
        btnPesquisar = findViewById(R.id.btnPesquisar);
        edtiPesquisar = findViewById(R.id.edtTituloPesquisa);
        rd_PesquisarPor = findViewById(R.id.radioGroup);
        btnRemover = findViewById(R.id.btnRemover);
        btnAtualizar = findViewById(R.id.btnAtualizar);

        btnCadastrar.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, CadastroActivity.class));
        });

        btnPesquisar.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, PesquisaActivity.class);
            intent.putExtra("tipo", rd_PesquisarPor.getCheckedRadioButtonId());
            intent.putExtra("busca", edtiPesquisar.getText().toString());
            startActivity(intent);
        });

        btnRemover.setOnClickListener(view -> {
            exibirDialogoId("Remover Livro", (dialog, which, id) -> {
                LivroDAO dao = new LivroDAO(MainActivity.this);
                dao.deletarRegistro(id);
                Toast.makeText(MainActivity.this, "Livro removido!", Toast.LENGTH_SHORT).show();
            });
        });

        btnAtualizar.setOnClickListener(view -> {
            exibirDialogoId("Atualizar Livro", (dialog, which, id) -> {
                Intent intent = new Intent(MainActivity.this, CadastroActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
            });
        });
    }

    // Método auxiliar para criar o diálogo de entrada de ID
    private interface DialogConfirmListener {
        void onConfirm(DialogInterface dialog, int which, int id);
    }

    private void exibirDialogoId(String titulo, DialogConfirmListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(titulo);
        builder.setMessage("Informe o ID do livro:");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setView(input);
        builder.setPositiveButton("OK", (dialog, which) -> {
            String text = input.getText().toString();
            if (!text.isEmpty()) {
                listener.onConfirm(dialog, which, Integer.parseInt(text));
            }
        });
        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }
}
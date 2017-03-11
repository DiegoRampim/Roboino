package br.com.roboino.bluetoothroboino;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import br.com.roboino.bluetoothroboino.dao.AcaoDAO;
import br.com.roboino.bluetoothroboino.modelo.Acao;

public class FormularioActivity extends AppCompatActivity {

    private FormularioHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario);

        helper = new FormularioHelper(this);

        Intent intent = getIntent();
        Acao acao = (Acao) intent.getSerializableExtra("acao");

        if(acao != null){
            helper.preencheFormulario(acao);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_formulario, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.menu_formulario_ok:

                Acao acao = helper.pegaAcao();
                AcaoDAO dao = new AcaoDAO(this);

                ///Toast.makeText(this, "Nome: "+acao.getNome().toString() + "\nComando: "+acao.getDado().toString(), Toast.LENGTH_LONG).show();


                if(acao.getId() != null){
                    dao.altera(acao);
                }else{
                    dao.insere(acao);
                }

                dao.close();
                finish();

                break;
        }


        return super.onOptionsItemSelected(item);
    }
}

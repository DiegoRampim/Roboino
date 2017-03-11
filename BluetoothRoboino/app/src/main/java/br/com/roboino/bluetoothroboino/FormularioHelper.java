package br.com.roboino.bluetoothroboino;

import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import br.com.roboino.bluetoothroboino.modelo.Acao;

/**
 * Created by Diego on 01/03/2017.
 */

public class FormularioHelper {

    private final EditText campoNome;
    private final EditText campoDado;

    private Acao acao;

    public FormularioHelper(FormularioActivity activity) {

        campoNome = (EditText) activity.findViewById(R.id.formulario_nome);
        campoDado = (EditText) activity.findViewById(R.id.formulario_dado);

        acao = new Acao();
    }

    public Acao pegaAcao(){

        //Toast.makeText(activity, "pegaAcao \nNome: " + campoNome.toString(), Toast.LENGTH_LONG).show();

        acao.setNome(campoNome.getText().toString());
        acao.setDado(campoDado.getText().toString());

        return acao;
    }

    public void preencheFormulario(Acao acao){
        campoNome.setText(acao.getNome());
        campoDado.setText(acao.getDado());

        this.acao = acao;
    }
}



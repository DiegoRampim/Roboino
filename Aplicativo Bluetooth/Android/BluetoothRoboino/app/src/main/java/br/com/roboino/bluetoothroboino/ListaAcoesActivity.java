package br.com.roboino.bluetoothroboino;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.pm.InstrumentationInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.UUID;

import br.com.roboino.bluetoothroboino.dao.AcaoDAO;
import br.com.roboino.bluetoothroboino.modelo.Acao;

import static br.com.roboino.bluetoothroboino.R.id.listaAcoes;

public class ListaAcoesActivity extends AppCompatActivity {

    private static final int SOLICITA_ATIVACAO = 1;
    private static final int SOLICITA_CONEXAO = 2;
    private static String MAC = null;

    boolean conexao = false;

    UUID MEU_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

    BluetoothAdapter meuBluetoothAdapter = null;
    BluetoothDevice meuDevice = null;
    BluetoothSocket meuSocket = null;

    ConnectedThread connectedThread;

    private ListView listaAcoes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_acoes);

        meuBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if(meuBluetoothAdapter == null){
            Toast.makeText(getApplicationContext(), "Seu dispositivo não possui Bluetooth", Toast.LENGTH_LONG).show();
        }else{
            if(!meuBluetoothAdapter.isEnabled()){
                Intent ativaBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(ativaBluetooth, SOLICITA_ATIVACAO);
            }
        }







        listaAcoes = (ListView) findViewById(R.id.listaAcoes);

        Button chamaFormulario = (Button) findViewById(R.id.chamaFormulario);
        chamaFormulario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent abreFomulario = new Intent(ListaAcoesActivity.this, FormularioActivity.class);
                startActivity(abreFomulario);
            }
        });

        listaAcoes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> lista, View item, int position, long id) {
                Acao acao = (Acao) listaAcoes.getItemAtPosition(position);

                if(conexao){
                    Toast.makeText(ListaAcoesActivity.this, "Envia: "+acao.getDado().toString(), Toast.LENGTH_LONG).show();
                    connectedThread.escrever(acao.getDado().toString());
                }else{
                    Toast.makeText(ListaAcoesActivity.this, "Você não esta conectado a um dispositivo!", Toast.LENGTH_LONG).show();
                }


            }
        });

       registerForContextMenu(listaAcoes);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_lista, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){

            case R.id.menu_lista_bt:

              if(conexao){
                  //desconectar
                  try{
                      meuSocket.close();
                      conexao = false;
                      Toast.makeText(getApplicationContext(), "Desconectado", Toast.LENGTH_LONG).show();

                  }catch (IOException erro){

                      Toast.makeText(getApplicationContext(), "Erro: " + erro, Toast.LENGTH_LONG).show();

                  }
              }else{
                  //conectar

                  Toast.makeText(ListaAcoesActivity.this, "Conectar", Toast.LENGTH_SHORT).show();

                  Intent abreLista = new Intent(ListaAcoesActivity.this, ListaDispositivos.class);
                  startActivityForResult(abreLista, SOLICITA_CONEXAO);
              }

            break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode){

            case SOLICITA_ATIVACAO:
                if(resultCode == Activity.RESULT_OK){
                    Toast.makeText(getApplicationContext(), "O Bluetooth foi ativado!", Toast.LENGTH_LONG).show();

                }else{
                    Toast.makeText(getApplicationContext(), "O Bluetooth não foi ativado, o app será desativado", Toast.LENGTH_LONG).show();
                    finish();
                }
                break;

            case SOLICITA_CONEXAO:
                if(resultCode == Activity.RESULT_OK){

                    MAC = data.getExtras().getString(ListaDispositivos.ENDERECO_MAC);

                    //Toast.makeText(getApplicationContext(), "MAC2: " + MAC, Toast.LENGTH_LONG).show();

                    meuDevice = meuBluetoothAdapter.getRemoteDevice(MAC);

                    try{

                        meuSocket = meuDevice.createRfcommSocketToServiceRecord(MEU_UUID);

                        meuSocket.connect();

                        conexao = true;

                        connectedThread = new ConnectedThread(meuSocket);
                        connectedThread.start();

                        Toast.makeText(getApplicationContext(), "Você foi conectado com: " + MAC, Toast.LENGTH_LONG).show();


                    }catch (IOException erro){

                        Toast.makeText(getApplicationContext(), "Ocorreu um erro: " + erro, Toast.LENGTH_LONG).show();
                        conexao = false;
                    }

                }else{
                    Toast.makeText(getApplicationContext(), "Falha ao obter MAC", Toast.LENGTH_LONG).show();

                }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        carregaLista();
    }



    private void carregaLista(){
        AcaoDAO dao = new AcaoDAO(this);
        List<Acao> acoes = dao.buscaAcoes();
        dao.close();

        ArrayAdapter<Acao> adapter = new ArrayAdapter<Acao>(this, android.R.layout.simple_list_item_1, acoes);
        listaAcoes.setAdapter(adapter);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, final ContextMenu.ContextMenuInfo menuInfo) {

        MenuItem editar = menu.add("Editar");
        MenuItem deletar = menu.add("Deletar");

        deletar.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
                Acao acao = (Acao) listaAcoes.getItemAtPosition(info.position);

                AcaoDAO acaoDAO = new AcaoDAO(ListaAcoesActivity.this);

                acaoDAO.deleta(acao);
                acaoDAO.close();

                carregaLista();

                return false;
            }
        });

        editar.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
                Acao acao = (Acao) listaAcoes.getItemAtPosition(info.position);

                Intent intentVaiParaFormulario = new Intent(ListaAcoesActivity.this, FormularioActivity.class);
                intentVaiParaFormulario.putExtra("acao", acao);
                startActivity(intentVaiParaFormulario);

                return false;
            }
        });


    }

    private class ConnectedThread extends Thread {

        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {

            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the input and output streams, using temp objects because
            // member streams are final
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) { }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        /*public void run() {
            byte[] buffer = new byte[1024];  // buffer store for the stream
            int bytes; // bytes returned from read()

            // Keep listening to the InputStream until an exception occurs
            while (true) {
                try {
                    // Read from the InputStream
                    bytes = mmInStream.read(buffer);
                    // Send the obtained bytes to the UI activity
                    mHandler.obtainMessage(MESSAGE_READ, bytes, -1, buffer)
                            .sendToTarget();
                } catch (IOException e) {
                    break;
                }
            }
        }*/

        /* Call this from the main activity to send data to the remote device */
        public void escrever(String texto) {

            byte[] msg = texto.getBytes();

            try {
                mmOutStream.write(msg);
            } catch (IOException e) { }
        }

    }
}

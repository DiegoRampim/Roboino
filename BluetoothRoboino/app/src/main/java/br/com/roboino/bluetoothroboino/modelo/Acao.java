package br.com.roboino.bluetoothroboino.modelo;

import java.io.Serializable;

/**
 * Created by Diego on 01/03/2017.
 */

public class Acao implements Serializable{

    private Long id;
    private String nome;
    private String dado;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDado() {
        return dado;
    }

    public void setDado(String dado) {
        this.dado = dado;
    }

    @Override
    public String toString() {
        return this.getNome();
    }
}

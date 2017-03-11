/*
 * Parte 2 - Codigo de Apoio
 * www.roboino.com.br
 * Diego Rampim
 */

#include <SoftwareSerial.h>

//Armazena o caracter recebido
char buf;
String conteudo = "";

SoftwareSerial novaSerial(5, 4); // rx, tx
 
void setup(){
  Serial.begin(4800);
  novaSerial.begin(9600);
}
 
void loop(){

  while(novaSerial.available() > 0){

    buf = novaSerial.read();
    conteudo.concat(buf);
       
    if (buf == '#'){
      conteudo.concat("\n");
      Serial.print("Conteudo: ");
      Serial.println(conteudo);
      conteudo = "";
     }
    }
}

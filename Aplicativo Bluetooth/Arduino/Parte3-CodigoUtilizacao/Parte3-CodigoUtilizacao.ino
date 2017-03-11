/*
 * Parte 3 - Codigo para utilização do Bluetooth - Controle de Leds e Reles
 * www.roboino.com.br
 * Diego Rampim
 */

#include <SoftwareSerial.h>

char buf;
String conteudo = "";

SoftwareSerial novaSerial(5, 4); // rx, tx

int led1 = 8;
int led2 = 9;
 
void setup(){
  
  Serial.begin(4800);
  novaSerial.begin(9600);

  pinMode(led1, OUTPUT);
  pinMode(led2, OUTPUT);

  digitalWrite(led1, LOW);
  digitalWrite(led2, LOW);
}
 
void loop(){

  while(novaSerial.available() > 0){

    buf = novaSerial.read();
    conteudo.concat(buf);
       
    if (buf == '#'){

      conteudo = conteudo.substring(0, conteudo.indexOf("#"));

          if(conteudo.equalsIgnoreCase("led1")){
            if(digitalRead(led1)){
              digitalWrite(led1, LOW);
            }else{
              digitalWrite(led1, HIGH);
            }
          }else{
            if(conteudo.equalsIgnoreCase("led2")){
              if(digitalRead(led2)){
                digitalWrite(led2, LOW);
              }else{
                digitalWrite(led2, HIGH);
              }
            }
          }

          conteudo = "";
    }
}
}

/*
 * Parte 1 - Configuração Bluetooth
 * www.roboino.com.br
 * Diego Rampim
 */
#include <SoftwareSerial.h>                           

SoftwareSerial MinhaSerial(5, 4); // RX, TX
String comando = "";   

void setup(){
  Serial.begin(4800);
  Serial.println("Digite os comandos AT :");
  MinhaSerial.begin(9600);
}
                                                      
void loop(){

  if (MinhaSerial.available()){

     while(MinhaSerial.available()){
       comando.concat((char) MinhaSerial.read());
     }

   Serial.println(comando);
   comando = "";
  }

  if (Serial.available())  {
    delay(10);
    MinhaSerial.write(Serial.read());
  }

}


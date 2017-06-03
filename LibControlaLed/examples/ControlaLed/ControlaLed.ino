/*
Exemplo de utilização LibControlaLed
http://roboino.com.br
contato@roboino.com.br
Diego Rampim
*/
#include <ControlaLed.h>

  ControlaLed led(13); //Start da biblioteca atribuindo um objeto (led) 
					   //e passando o numero do pino.

void setup() {}

void loop() {
  
  led.acende(); //Chamando o metodo acende.
  
  delay(500);
  
  led.apaga();	//Chamando o metodo apaga.
  
  delay(500);

  led.setDelay(1000);	//Configurando o tempo de delay.

  led.pisca();	//Chamando o metodo pisca.
}

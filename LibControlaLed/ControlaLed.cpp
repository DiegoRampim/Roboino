#include "ControlaLed.h"

 ControlaLed::ControlaLed(int pino){
    delay_ = 500;
    pino_ = pino;

    pinMode(pino_, OUTPUT);
    digitalWrite(pino_, LOW);
  }

  void ControlaLed::setDelay(int tempo){
    delay_ = delay;
  }

  void ControlaLed::acende(){
    digitalWrite(pino_, HIGH);
  }

  void ControlaLed::apaga(){
    digitalWrite(pino_, LOW);
  }

  void ControlaLed::pisca(){
    acende();
    delay(delay_);
    apaga();
    delay(delay_);
  }

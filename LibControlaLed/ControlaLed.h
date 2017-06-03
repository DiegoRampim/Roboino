#ifndef CONTROLALED_h
  #define CONTROLALED_h

    #include <Arduino.h>

    class ControlaLed{
      public:
        ControlaLed(int pino);
        void acende();
        void apaga();
        void pisca();
        void setDelay(int tempo);

      private:
        int pino_;
        int delay_;
    };

#endif

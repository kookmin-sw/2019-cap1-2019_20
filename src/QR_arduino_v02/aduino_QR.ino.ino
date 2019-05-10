#include "qrcode.h"

void setup() {
    Serial.begin(9600);

    // Start time
    uint32_t dt = millis();
  int Button =7 //버튼 포트7번으로
  int button_count =0;
  int button_state =0;
  int last_button_state =0;
  
    // Create the QR code
    QRCode qrcode;
    uint8_t qrcodeData[qrcode_getBufferSize(3)];

//일단 인증용으로 사용할 1000개의 데이터 데이터값은 각 0~999까지로 한다.
    int test_text[1000];
    for(int i=0; i<1000; i++)
    {
      test_text[i] =i;
      }


}

void loop() {
  button_state = digitalRead(Button);
  if(button_state != last_button_state)
  {
    if(button_state ==HIGH)
    {
      delay(100);
      
    //여기부터 버튼 클릭시 이벤트 발생
    
    qrcode_initText(&qrcode, qrcodeData, 3, 0, test_text[button_count]);
  
    // Delta time
    dt = millis() - dt;
    Serial.print("QR Code Generation Time: ");
    Serial.print(dt);
    Serial.print("\n");

    // Top quiet zone
    Serial.print("\n\n\n\n");

    for (uint8_t y = 0; y < qrcode.size; y++) {

        // Left quiet zone
        Serial.print("        ");

        // Each horizontal module
        for (uint8_t x = 0; x < qrcode.size; x++) {

            // Print each module (UTF-8 \u2588 is a solid block)
            Serial.print(qrcode_getModule(&qrcode, x, y) ? "\u2588\u2588": "  ");

        }

        Serial.print("\n");
    }

    // Bottom quiet zone
    Serial.print("\n\n\n\n");

    //출력하고 button_count +1해준다.
      button_count++;
    }
   }

}

#include <ESP8266WiFi.h>
#include <Adafruit_MQTT_Client.h>

#define wifi "Srijha"
#define password "sailesh0906"
#define server "io.adafruit.com"
#define port 1883
#define username "aish_raj"
#define key "5d4b96bcd7ae460fb1fdfe0c0fe4b75c"

String ppm, qIndex, ppm1, h, t, hi;

WiFiClient esp;

Adafruit_MQTT_Client mqtt(&esp,server,port,username,key);
Adafruit_MQTT_Publish ppm_ = Adafruit_MQTT_Publish(&mqtt,username"/feeds/Gas_data");
Adafruit_MQTT_Publish qIndex_ = Adafruit_MQTT_Publish(&mqtt,username"/feeds/qindex");
Adafruit_MQTT_Publish ppm1_ = Adafruit_MQTT_Publish(&mqtt,username"/feeds/ppm1");
Adafruit_MQTT_Publish h_ = Adafruit_MQTT_Publish(&mqtt,username"/feeds/h");
Adafruit_MQTT_Publish t_ = Adafruit_MQTT_Publish(&mqtt,username"/feeds/temp");
Adafruit_MQTT_Publish hi_ = Adafruit_MQTT_Publish(&mqtt,username"/feeds/hi");
 
void setup() {
  // Initialize Serial port
  Serial.begin(9600);
  Serial.readString();

  WiFi.begin(wifi,password);

  while(WiFi.status()!=WL_CONNECTED)
  {
     delay(500);
     Serial.print(".");
  }

  Serial.println("WiFi connected");
  Serial.println("IP Address: ");
  Serial.println(WiFi.localIP());
  Serial.print("Connecting to MQTT");

  while(mqtt.connect())
  {
    Serial.print(".");
  }

  Serial.println("MQTT connected");
}
 
void loop() {

  if(Serial.available()){  
    if(firstCheck()){
      readData();
    }
  }
}

bool firstCheck(){
  String temp=Serial.readString();
  temp.trim();
  return temp.equals("*");
}

void readData(){
  while(!Serial.available()){}
    ppm=Serial.readString();
    ppm.trim();
    ppm_.publish(ppm.toFloat());
   
    while(!Serial.available()){}
    qIndex=Serial.readString();
    qIndex.trim();
    qIndex_.publish(qIndex.toFloat());
   
    while(!Serial.available()){}
    ppm1=Serial.readString();
    ppm1.trim();
    ppm1_.publish(ppm1.toFloat());
   
    while(!Serial.available()){}
    h=Serial.readString();
    h.trim();
    h_.publish(h.toFloat());
   
    while(!Serial.available()){}
    t=Serial.readString();
    t.trim();
    t_.publish(t.toFloat());
   
    while(!Serial.available()){}
    hi=Serial.readString();
    hi.trim();
    hi_.publish(hi.toFloat());
}




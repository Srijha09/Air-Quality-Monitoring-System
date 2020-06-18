#include <SoftwareSerial.h>
SoftwareSerial s(5,6); //D6 to RX of node mcu
#include <DHT.h>
#include <DHT_U.h>
// Example testing sketch for various DHT humidity/temperature sensors
// Written by ladyada, public domain
#include "DHT.h"
#define DHTPIN A0     // what pin we're connected to
#define DHTTYPE DHT11   // DHT 11
DHT dht(DHTPIN, DHTTYPE, 6);

int qIndex;
int gas_sensor = A1; //Sensor pin
float m = -0.3376; //Slope
float b = 0.7165; //Y-Intercept
float R0 = 2.82; //Sensor Resistance in fresh air from previous code


int CO_sensor = A2; //Sensor pin
float m1 = -0.6527; //Slope
float b1 = 1.30; //Y-Intercept
float R01 = 7.22; //Sensor Resistance


void setup() {
  Serial.begin(9600);
  s.begin(9600);
//  Serial.println("DHTxx test!");
  pinMode(gas_sensor,INPUT);
  pinMode(CO_sensor,INPUT);
  dht.begin();
}

void loop() {
 
  float sensor_volt; //Define variable for sensor voltage
  float RS_gas; //Define variable for sensor resistance  
  float ratio; //Define variable for ratio
  float sensorValue = analogRead(gas_sensor); //Read analog values of sensor  
  sensor_volt = sensorValue*(5.0/1023.0); //Convert analog values to voltage
  RS_gas = ((5.0*10.0)/sensor_volt)-10.0; //Get value of RS in a gas
  ratio = RS_gas/R0;  // Get ratio RS_gas/RS_air
  double ppm_log = (log10(ratio)-b)/m; //Get ppm value in linear scale according to the the ratio value  
  double ppm = pow(10, ppm_log); //Convert ppm value to log scale
  //Serial.print("Our desired ppm:");
  //Serial.print(ppm);
   
      if (ratio<0.75)
          qIndex = 5;
      else if (ratio>0.75 && ratio<1)
          qIndex = 4;
      else if (ratio>1 && ratio<1.25)
          qIndex = 3;
      else if (ratio>1.25 && ratio<1.5)
          qIndex = 2;
      else if (ratio>1.5)
          qIndex = 1;
//  Serial.print(" AQI Index:");
//  Serial.println(qIndex);
 
  float sensor_volt1; //Define variable for sensor voltage
  float RS_gas1; //Define variable for sensor resistance  
  float ratio1; //Define variable for ratio
  float sensorValue1 = analogRead(CO_sensor); //Read analog values of sensor  
  sensor_volt1 = sensorValue1*(5.0/1023.0); //Convert analog values to voltage
  RS_gas1 = ((5.0*10.0)/sensor_volt1)-10.0; //Get value of RS in a gas
  ratio1 = RS_gas1/R01;  // Get ratio RS_gas/RS_air
  double ppm_log1 = (log10(ratio1)-b1)/m1; //Get ppm value in linear scale according to the the ratio value  
  double ppm1 = pow(10, ppm_log1); //Convert ppm value to log scale
//  Serial.print("CO PPM = ");
//  Serial.println(ppm1);
//  delay(1000);
//      if (ratio1<50)
//        Serial.println("Low Level");
//      else if (ratio1>51 && ratio1<100)
//        Serial.println("Mid Level");
//      else if (ratio1>101)
//        Serial.println("High Level");

  float h = dht.readHumidity();
  float t = dht.readTemperature();
  float f = dht.readTemperature(true);

  float hi = dht.computeHeatIndex(f, h);

   s.println("*");
    delay(1000);
s.println(ppm);
delay(1000);
s.println(qIndex);
delay(1000);
s.println(ppm1);
delay(1000);
s.println(h);
delay(1000);
s.println(t);
delay(1000);
s.println(hi);

delay(1000);
}


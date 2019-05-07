#include <FreqCounter.h>


signed long freq, prev_freq = 0, freq_diff;
int mc_ctr = 0, lmv_ctr = 0;
int pinLed=13;

#define MC_THRESH 10
#define LMV_THRESH 150
#define MAX_VEH_THRESH 3000

boolean pos_slope = false, old_pos_slope = false, detecting_mc = false, detecting_lmv = false;

void setup() 
{

  pinMode(pinLed, OUTPUT);
  Serial.begin(115200);        // connect to the serial port

//  Serial.println("Vehicle Detector");


}



void loop() 
{
  // wait if any serial is going on
  FreqCounter::f_comp=10;   // Cal Value / Calibrate with professional Freq Counter
  FreqCounter::start(500);  // 100 ms Gate Time
  while (FreqCounter::f_ready == 0); 

  freq=(FreqCounter::f_freq)*2;

//  Serial.print("Freq diff= ");
//  Serial.print(prev_freq - freq);
//  Serial.print("   Freq = ");  
//  Serial.println(freq);
  
  if (freq  - prev_freq> MAX_VEH_THRESH)
  {
    prev_freq = freq;
  }
  
  freq_diff = freq - prev_freq;

  if (freq_diff > 0)
  {
    pos_slope = true;
  }
  else
  {
    pos_slope = false;
  }
  
  if (freq_diff > MC_THRESH && freq_diff < LMV_THRESH)
  {
    detecting_mc = true;
    detecting_lmv = false;
  }
  else if ( freq_diff > LMV_THRESH && freq_diff < MAX_VEH_THRESH)
  {
    detecting_lmv = true;
    detecting_mc = false;
  }
  if (!pos_slope && old_pos_slope && detecting_mc)
  {
    mc_ctr++;
    Serial.print("t");
    Serial.print("0");
//    Serial.print(mc_ctr);
//    Serial.println(" two wheelers detected");
    detecting_mc = false;
    detecting_lmv = false;
  }
  else if (!pos_slope && old_pos_slope && detecting_lmv)
  {
    lmv_ctr++;
    Serial.print("f");
    Serial.print("0");
//    Serial.print(lmv_ctr);
//    Serial.println(" four wheelers detected");
    detecting_lmv = false;
    detecting_mc = false;
  }
    
  prev_freq = freq;
  old_pos_slope = pos_slope;
}


clc;
clear;
close all;

Velocity=0;
Altitude=0;
Temperature=24;
Humidity=100;
Pressure=100000;
x=0;
y=0;
tic;

while x~=10000
   Velocity=Velocity+0.1;
   Altitude=Altitude+10;
   Temperature=Temperature-0.01;
   Humidity=Humidity-0.005;
   Pressure=(100000)*exp(-Altitude/10000);
   Time_int=clock;
   fix(Time_int);
   Time1=Time_int(4);
   Time2=Time_int(5)/100;
   Time=Time1+Time2;   
   t=floor(toc);   
   x=x+1;
   y=y+1;
   
   tempFileName= sprintf('temp%d.txt', y);
   tempFile = fopen(tempFileName,'w');
   velocityFileName= sprintf('velocity%d.txt', y);
   velocityFile = fopen(velocityFileName,'w');
   humidityFileName= sprintf('humidity%d.txt', y);
   humidityFile = fopen(humidityFileName,'w');
   pressureFileName= sprintf('pressure%d.txt', y);
   pressureFile = fopen(pressureFileName,'w');
  
   fprintf(tempFile, '%s,' , 'temperature');
   fprintf(tempFile, '%4.2f,' , Altitude);
   fprintf(tempFile, '%4.2f,' , Temperature);
   fprintf(tempFile, '%4.2f' , Temperature+20);
   
   fprintf(velocityFile, '%s,' , 'velocity');
   fprintf(velocityFile, '%4.2f,' , Altitude);
   fprintf(velocityFile, '%4.2f,' , Velocity);
   fprintf(velocityFile, '%4.2f' , Velocity+40);
   
   fprintf(humidityFile, '%s,' , 'humidity');
   fprintf(humidityFile, '%4.2f,' , Altitude);
   fprintf(humidityFile, '%4.2f,' , Humidity);
   fprintf(humidityFile, '%4.2f' , Humidity+10);
   
   fprintf(pressureFile, '%s,' , 'pressure');
   fprintf(pressureFile, '%4.2f,' , Altitude);
   fprintf(pressureFile, '%4.2f,' , Pressure);
   fprintf(pressureFile, '%4.2f' , Pressure+1000);
   
   fclose(tempFile);
   fclose(velocityFile);
   fclose(humidityFile);
   fclose(pressureFile);
   
   pause(0.1);  
   
end
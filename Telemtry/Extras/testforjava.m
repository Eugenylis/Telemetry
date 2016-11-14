clc;
clear;
close all;

Velocity=0;
Altitude=0;
Temperature=24;
x=0;
y=0;
tic;

while x~=1000
   Velocity=Velocity+1;
   Altitude=Altitude+10;
   Temperature=Temperature-0.1;
   Time_int=clock;
   fix(Time_int);
   Time1=Time_int(4);
   Time2=Time_int(5)/100;
   Time=Time1+Time2;   
   t=floor(toc);   
   x=x+1;
   y=y+1;
   foldername= sprintf('test%d.txt', y);
   file = fopen(foldername,'w');   
   header1='%4.2f,';
   header2='%4.2f,';
   header3='%4.2f,';
   header4='%4.2f,';
   header5='%4.2f,';
  
   fprintf(file, header1 , Velocity);
   fprintf(file, header2 , Altitude);
   fprintf(file, header3, Temperature);
   fprintf(file, header4, Time);
   fprintf(file, header5, t);
   fclose(file);
   pause(2);  
   
end
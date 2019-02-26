clc; clear; close all;

y0 = getsignal(imread('C:\Users\Nikunj\Dropbox\BEL\Signal Comparison\Untitled1.png'));
y1 = getsignal(imread('C:\Users\Nikunj\Dropbox\BEL\Signal Comparison\Untitled2.png'));

dim = size(y0);
x = 1:dim(2);

thresh1 = 0.5*max(y0);
thresh2 = 0.5*max(y1);

out1 = y0>thresh1;
out2 = y1>thresh2;

clf;
figure(1);
subplot(2, 2, 1), plot(x, y0), title('Signal 1'), xlabel('time'), ylabel('Amplitude');
subplot(2, 2, 3), plot(x, out1), title('output 1'), xlabel('time'), ylabel('Amplitude'), axis([-Inf Inf 0 2]);
subplot(2, 2, 2), plot(x, y1), title('Signal 2'), xlabel('time'), ylabel('Amplitude');
subplot(2, 2, 4), plot(x, out2), title('output 2'), xlabel('time'), ylabel('Amplitude'), axis([-Inf Inf 0 2]);

sensor_dist = 2; % Distance between sensor nodes = 2m
t1 = Inf;
t2 = Inf;
t3 = Inf;
for i=1:dim(2)-1
    if (t1 == inf) & (out1(i)==1 & out1(i+1)==0)
        t1 = i * 20e-3;
    end
    if (t1 < inf) & (out1(i)==0 & out1(i+1)==1)
        t2 = i * 20e-3;
    end
    if (t1 < inf) & (out1(i)==1 & out1(i+1)==0)
        t3 = i * 20e-3;
    end
end

speed = sensor_dist / (t3-t1) * 3.6;
veh_length = (t3-t2) * speed / 3.6;
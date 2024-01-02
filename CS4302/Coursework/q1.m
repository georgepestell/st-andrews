% Read the file
% y = samples
% Fs = sampling frequency
clearvars;
close all;
[y,Fs] = audioread("Audio/audio_in_noise1.wav");

% Extract a single channel
y = y(:,1);

% Total number of samples
N = size(y,1);

% Plot the base signal
plot(1:N, y);
title("Left Channel")

%% 

% Frequency samples
df = Fs / N;
w = (-(N/2):(N/2)-1)*df;

y_fft = fft(y, N) / N;
y_fftShift = fftshift(y_fft);

figure; plot(w, abs(y_fftShift));

%% 

% Create a band pass
n = 7;
beginFreq = 599/(Fs/2);
endFreq =  601/(Fs/2);
[b,a] = butter(n, [beginFreq, endFreq], 'bandpass');

% Filter the signal
new_y = filter(b,a,y);

sound(new_y, Fs);

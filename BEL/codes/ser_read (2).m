clc; clear; close all;
s = serial('COM3', 'BaudRate', 115200, 'DataTerminalReady', 'off', 'RequestToSend', 'off', 'Terminator', 'NUL');
fclose(s);
fopen(s);
if (strcmp(s.Status, 'open'))
    for i=1:1024
        A = fscanf(s);
        [val, ok] = str2num(A);
        if (ok == 1)
            buf(i) = val;
        else
            if (i == 1)
                buf(i) = 0;
            else
                buf(i) = buf(i-1);
            end
        end
    end
else
    A = 'closed';
end
fclose(s);
delete(s);
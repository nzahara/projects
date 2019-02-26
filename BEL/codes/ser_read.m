clc; clear; close all;
s = serial('COM3', 'BaudRate', 115200, 'DataTerminalReady', 'off', 'RequestToSend', 'off', 'Terminator', 'NUL');
fclose(s);
fopen(s);
if (strcmp(s.Status, 'open'))
    while(1)
        j = 1;
        k = 1;
        buf1(1) = 0;
        buf2(1) = 0;
        for i=1:256
            A = fscanf(s);
            [val, ok] = str2num(A);
            if (ok == 1)
                if mod(i,2)==0
                    buf1(j) = val;
                    j = j+1;
                    plot(buf2,'DisplayName','buf2','YDataSource','buf2');hold all;plot(buf1,'DisplayName','buf1','YDataSource','buf1');hold off;figure(gcf);
                else
                    buf2(k) = val;
                    k = k+1;
                    plot(buf2,'DisplayName','buf2','YDataSource','buf2');hold all;plot(buf1,'DisplayName','buf1','YDataSource','buf1');hold off;figure(gcf);
                end
            else
                if (j == 1)
                    buf1(i) = 0;
                    plot(buf2,'DisplayName','buf2','YDataSource','buf2');hold all;plot(buf1,'DisplayName','buf1','YDataSource','buf1');hold off;figure(gcf);
                elseif (k == 1)
                    buf2(i) = 0;
                    plot(buf2,'DisplayName','buf2','YDataSource','buf2');hold all;plot(buf1,'DisplayName','buf1','YDataSource','buf1');hold off;figure(gcf);
                else
                    buf1(j) = buf1(j-1);
                    buf2(k) = buf2(k-1);
                    plot(buf2,'DisplayName','buf2','YDataSource','buf2');hold all;plot(buf1,'DisplayName','buf1','YDataSource','buf1');hold off;figure(gcf);
                end
            end
        end
        'test'
        %         hold on;
    end
else
    A = 'closed';
end
fclose(s);
delete(s);
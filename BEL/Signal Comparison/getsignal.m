function sgl = getsignal(im)
im = im2bw(im, 0.5);
dim = size(im);
for i = 1:dim(2)
    j = 1;
    while im(j,i) ~= 0
        j = j + 1;
        if j > dim(1)
            j = 0;
            break;
        end
    end
    sgl(i) = j;
end

function [ w R ] = least_sequare_comp(z,y)

s = z*y;

M = z*transpose(z);
w=M\s;
R=sum((transpose((transpose(w)*z)) -y).^2);

end
function [ A,b ] = make_triangle( A,b )
%UNTITLED8 Summary of this function goes here
%   Detailed explanation goes here
N = length(A);
for i=1:N
    for j=i+1:N
    c = -A(j,i)/A(i,i);
    A(j,:) = A(j,:) + A(i,:)*c;
    b(j) = b(j)+ c*b(i);
    end
end


end


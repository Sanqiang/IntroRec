function [ X ] = Back_substitution( A,b )
%UNTITLED2 Summary of this function goes here
%   Detailed explanation goes here
N = length(A);
X=zeros(N,1);
for i=N:-1:1
    c= b(i);
    for j=i+1:N
        c=c-A(i,j)*X(j);
    end
    X(i)= c/A(i,i);

end



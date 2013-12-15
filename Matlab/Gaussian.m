function [ x ] = Gaussian( A,b )
%UNTITLED9 Summary of this function goes here
%   Detailed explanation goes here
[A b] = make_triangle(A,b);
[x] = Back_substitution(A,b);

end


function [R ] = Evaluate( w,z,y )
%UNTITLED15 Summary of this function goes here
%   Detailed explanation goes here

y_predicted= transpose(w)*z;
R= (transpose(y)-y_predicted).^2;
R= mean(R);
end

 
function [   ] = Poly_Select( )
%UNTITLED17 Summary of this function goes here
%   Detailed explanation goes here
p=8;
traindata = importdata('traindata.txt');
subTrainData=traindata(1:926,:);
subTrainData= subTrainData';
X = subTrainData(1:8,:); Y = subTrainData(9,:);
[n m]=size(X);
z=zeros(n*(p+1),m);z(1:n,:) = ones(n,m); 

best_p_train=0;best_p_test=0;
    
for j=1:p
      z(j*8+1:(j+1)*8,:) = X.^(j);
end
   % disp(Y);
for i=1:p
   z_= z(1:((i+1)*8),:);
   [best_p_tr best_p_te]= Solve(z_,Y);
   best_p_train(i) = best_p_tr;
   best_p_test(i)= best_p_te;
end
for i=1:length(best_p_train)
    fprintf('%i\n   ',i);fprintf('%i train_avg\n   ',best_p_train(i));fprintf('%i test_avg\n ',best_p_test(i));
    
end
[C,I] = min(best_p_test);
disp('best_p');disp(I);

x=z(1:((I+1)*8),:);
%disp(x);
[w R] = least_sequare_comp(x,Y');
disp(size(w));disp(size(x));
testdata = importdata('testinputs.txt');
x= testdata;
y= w*x;
disp(y);
end


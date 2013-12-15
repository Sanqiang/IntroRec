k = 10; %k-fold

traindata = importdata('traindata.txt');
x = traindata(:,1:8);
y = traindata(:,9);
testdata = importdata('testinputs.txt');

theta = [];
loss = [];

gc = m/k;

for k_ind = 1 : k
    
    k_x = [x(1:gc*(k_ind-1),:);x(gc*(k_ind+1):end,:)];
    k_y = [y(1:gc*(k_ind-1),:);y(gc*(k_ind+1):end,:)];
    len = size(k_x,1);
    
    
    k_test_x = x(1:gc*(k_ind),:);
    k_test_y = y(1:gc*(k_ind),:);
    
    
    theta(:,k_ind) = pinv(k_x' * k_x) * k_x' * k_y;
    
    k_predict = k_test_x * theta(:,k_ind);
    
    loss(:,k_ind) = sum((k_predict - k_test_y).^2)/len;
    
end

[best_loss, best_ind] = min(loss);
best_theta = theta(:,best_ind);
fprintf('Best Loss equal %f \n', best_loss);

predict = testdata * best_theta;
fprintf('example predict for 1 2 3:\n %f \n %f \n %f \n',predict(1),predict(2),predict(3));
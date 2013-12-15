function [best_P_Train best_P_Test] = Solve(subTrainData,Y)

  [n m] = size(subTrainData);
  N= n; K = 5; Y=Y';
for k=1:K
    
    test_start = floor(1+(k-1)*N/K);
    test_end   = floor(k*N/K);
    who_test  = test_start:test_end;
    who_train = [1:test_start-1 test_end+1:N];
    Train_Data =subTrainData(who_train,:);
    Test_Data =subTrainData(who_test,:);
  who_train_=who_train;who_test_=who_test;
  for e=1:length(who_train)
      if who_train(e)>length(Y)
          who_train_(e)=1;
      end
  end
   for e=1:length(who_test)
      if who_test(e)>length(Y)
          who_test_(e)=1;
      end
  end
  

    Y_Train=Y(who_train_);
    Y_Test=Y(who_test_);
    [w R] = least_sequare_comp(Train_Data',Y_Train);
    R_Train(k)= R;
    R_Test(k) = Evaluate(w,Test_Data',Y_Test);
  
end
best_P_Train = mean(R_Train);
best_P_Test = mean (R_Test);



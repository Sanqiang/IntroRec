function mean = mean(data)
    num_movie = size(data,2);

    denominor = zeros(1,num_movie);
    
    for ind = 1 : num_movie
       cur_movie = data(:,ind); 
       denominor(ind) = length(find(cur_movie > 0));
    end
    
    mean = sum(data)./denominor;
    

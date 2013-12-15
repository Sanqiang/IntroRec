function score = fourhigh(data)
    num_movie = size(data,2);

    nomitor = zeros(1,num_movie);
    denominor = zeros(1,num_movie);
    
    for ind = 1 : num_movie
       cur_movie = data(:,ind);
       nomitor(ind) = length(find(cur_movie >= 4));
       denominor(ind) = length(find(cur_movie > 0));
    end
    
    score = nomitor./denominor;
    

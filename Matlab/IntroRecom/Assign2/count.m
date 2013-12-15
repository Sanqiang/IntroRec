function score = count(data)
    num_movie = size(data,2);

    for ind = 1 : num_movie
       cur_movie = data(:,ind);
       score(ind) = length(find(cur_movie > 0));
    end
    

    

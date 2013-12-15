function score = StarWar(data)
    num_movie = size(data,2);
    num_person = size(data,1);
    
    score = zeros(1, num_movie);
    
    bicount = zeros(1, num_movie);

    sw_movie = data(:,1);
    person_who_rate_sw = find(sw_movie > 0);
    x_count = length(person_who_rate_sw);
    px = x_count / num_person;
    
    for i_m = 2 : num_movie
        sw_movie = data(:,i_m);
        person_who_rate_this = find(sw_movie > 0); 
        bicount(i_m) = length(intersect(person_who_rate_this,person_who_rate_sw));
    end
    
    score = (bicount./num_person) ./ px;

    

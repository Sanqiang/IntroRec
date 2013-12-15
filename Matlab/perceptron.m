clear;
data = load('linearclass.txt');
x = data(:,1:2);%[1 1 ; 1 0 ; 1 0.5];%
y = data(:,3);%[1 -1 -1];%
y(y==0) = -1;
space_x1 = linspace(min(x(:,1)),max(x(:,1)),100);%linspace(-2,2,20);

len = length(y);

%%initialize
theta= y(1) * x(1,:);

%%loop
for loop = 1 : 1000
    for i = 1 : len
       if y(i) == 1
           plot(x(i,1),x(i,2),'rx');
       else
           plot(x(i,1),x(i,2),'gx');
       end
       hold on;

       if y(i) * (theta * x(i,:)') < 0
           theta = theta + y(i) * x(i,:);
       end


       %space_x2 = -space_x1 * theta(1) / theta(2);
       %plot(space_x1, space_x2);

       %pause;
    end
end

       space_x2 = -space_x1 * theta(1) / theta(2);
       plot(space_x1, space_x2,'b-');

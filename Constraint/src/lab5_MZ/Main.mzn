include "globals.mzn";

% _parameters_

int: n;
int: n_commercial;
int: n_residential;
array[0..n] of int: point_distribution;

% _variables_

% 0 commerical, 1 residential
array[1..n, 1..n] of var 0..1: spots; 

% scores
array[1..n] of var int: sum_row; 
array[1..n] of var int: sum_col; 
array[1..n] of var int: score_row; 
array[1..n] of var int: score_col; 
var int: score;

% _constraints_

% sum up nbr of residentials per rows and columns
constraint forall (i in 1..n)(sum_row[i] = sum(j in 1..n)(spots[i,j]));
constraint forall (i in 1..n)(sum_col[i] = sum(j in 1..n)(spots[j,i]));

% make sure the amount of residential plots doesn't exceed the available amount of residential plots.
constraint n_residential = sum(i in 1..n)(sum_row[i]);

% get point value for each row
constraint forall(i in 1..n)(score_row[i] = point_distribution[sum_row[i]]);

% get point value for each column
constraint forall(i in 1..n)(score_col[i] = point_distribution[sum_col[i]]);

% Calculate the total score.
constraint score = sum(i in 1..n)(score_row[i])+sum(i in 1..n)(score_col[i]);

% Lex2 enables lexicographic and more importantly, blocks symmetrical solution (improves performance for case 1 and 2, case 3 will never finish without it)
constraint lex2(spots);

solve maximize score;

output["points ",show(score), "\n"] ++ [show_int(1,spots[i,j]) ++ " " ++ if j mod n == 0 then "\n" else "" endif | i,j in 1..n];
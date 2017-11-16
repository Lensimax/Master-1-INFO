
set autoscale
set autoscale fix
set title "Temps de tri en focntion de la taille du vecteur"
set xlabel "Taille du vecteur"
set output 'Test/graph.png'
plot "Test/resultat" using 1:3
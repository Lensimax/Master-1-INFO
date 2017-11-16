#!/bin/sh

# Variables

NBTEST=30
NBTHREAD_MAX=16


# 50 100 1000 5000 10000 50000 100000 500000 1000000 10000000

rm -f Test/*

# Debut des test 

for size in 50 100 1000 5000 10000 # variation de la taille
do
	./creer_vecteur --size $size > Test/vecteur.txt

	# sequentiel

	cpt_seq=0

	for i in `seq 1 $NBTEST`
	do
		time_seq=`./tri_sequentiel --quiet --rusage < Test/vecteur.txt`
		# echo $time_seq
		cpt_seq=$(($cpt_seq+$time_seq))
	done

	avg_seq=$(($cpt_seq/$NBTEST))
	# echo "Moyenne en sequentiel pour vecteur de " $size ": " $avg_seq

	printf $size >> Test/resultat
	printf " " >> Test/resultat
	printf $avg_seq >> Test/resultat
	printf " " >> Test/resultat
	

	# threadé 
	# on fait varier le nombre de thread 

	for nb_thread in `seq 1 $NBTHREAD_MAX`
	do
		cpt_thr=0

		for j in `seq 1 $NBTEST`
		do
			time_thr=`./tri_threads --quiet --rusage --parallelism $nb_thread < Test/vecteur.txt`
			# echo $time_thr
			cpt_thr=$(($cpt_thr+$time_thr))

		done

		avg_thr=$(($cpt_thr/$NBTEST))

		# echo "Moyenne en threadé pour vecteur de " $size "avec " $nb_thread  "thread: " $avg_thr
		printf $avg_thr>> Test/resultat
		printf " " >> Test/resultat
	done
	
	printf "\n" >> Test/resultat

done 



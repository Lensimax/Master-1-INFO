#!/bin/sh

bornesup=4

for j in `seq 1 4`
do
	cpt_seq=0
	cpt_thr=0

	echo '' > Stat$j.txt

	for i in `seq 1 $bornesup`
	do
		./creer_vecteur --size 10000  >vecteur.txt
		seq=`./tri_sequentiel --quiet --rusage <vecteur.txt`
		# >> Stat$j.txt
		thr=`./tri_threads --quiet --rusage --parallelism $j <vecteur.txt`
		echo  $seq $thr >> Stat$j.txt

		cpt_seq=$(($cpt_seq+$seq))
		cpt_thr=$(($cpt_thr+$thr))
	done

	echo "avg seq" $(($cpt_seq/4))
	echo "avg thr" $(($cpt_thr/4))

done 



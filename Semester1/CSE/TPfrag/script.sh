#! /bin/bash
#make test_ls | tee res

echo "USER;TAILLE;TEMPS(ns)" > resultat
while read line  
do
 	    echo -n $line | cut -d "@" -f2 | grep ^[0-9] >>resultat
done < res



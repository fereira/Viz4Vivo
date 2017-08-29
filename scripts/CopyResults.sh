#!/bin/sh
currentDate=`date "+%Y-%m-%d%n"`
fromDir=/cul/data/vivoviz/POSTPROCESS_RESULTS/$currentDate
toDir=/usr/local/vivo-1.10/home/visualizationData
cp -f $fromDir/kwclouds/*.json $toDir
# cp -f $fromDir/collab/internal/* $toDir
cp -f $fromDir/collab/external/* $toDir
cp -f $fromDir/inferredkeywords/* $toDir
cp -f $fromDir/subjectarea/* $toDir


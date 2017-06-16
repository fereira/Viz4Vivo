#!/bin/bash
PROJECT_DIR=/usr/local/src/Viz4Vivo 
export PATH=$PATH
export CLASSPATH=$CLASSPATH:$PROJECT_DIR/build/classes:$PROJECT_DIR/lib/*
# echo $CLASSPATH
export JAVA_OPTS=-Xmx1024M
java $JAVA_OPTS -cp $CLASSPATH  edu.cornell.mannlib.viz.VizSparqlRunner $*





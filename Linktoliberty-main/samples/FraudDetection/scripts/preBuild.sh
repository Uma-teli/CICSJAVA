#!/bin/env sh

gitLocalDir=$1
previousJenkinsWorkspace=$2

MODEL_VERSION=$(python $gitLocalDir/samples/FraudDetection/scripts/json_extract.py model_version $previousJenkinsWorkspace)

$gitLocalDir/samples/FraudDetection/scripts/extract.sh $MODEL_VERSION $gitLocalDir $previousJenkinsWorkspace

$gitLocalDir/samples/FraudDetection/scripts/changeScoringEnd.sh $gitLocalDir $previousJenkinsWorkspace

$gitLocalDir/samples/FraudDetection/scripts/gitCommit.sh $gitLocalDir

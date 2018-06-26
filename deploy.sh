#!/bin/bash

set -e

validate () {
  echo "staging production" | grep -F -q -w "$1";
}

deploy () {
  IMAGE="madumbo:languagetool-$1"
  echo "Building $IMAGE"

  TARGET_DIR="languagetool-standalone/target/LanguageTool-3.9-SNAPSHOT/LanguageTool-3.9-SNAPSHOT/"

  echo "Removing $TARGET_DIR"
  rm -rf $TARGET_DIR
  build

  cp Dockerfile $TARGET_DIR
  cp config.properties $TARGET_DIR
  cd $TARGET_DIR

  eval $(aws --region eu-west-1 ecr get-login --no-include-email)
  docker build -t $IMAGE .
  docker tag $IMAGE 866953695171.dkr.ecr.eu-west-1.amazonaws.com/$IMAGE
  docker push 866953695171.dkr.ecr.eu-west-1.amazonaws.com/$IMAGE

  echo "Image pushed"
}


build () {
  echo "Building target"
  ./build.sh languagetool-standalone package -DskipTests
}


if validate "$1"
then
  deploy "$1"
else
  echo "Invalid deploy mode $1"
fi

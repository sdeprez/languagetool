#!/bin/bash

set -e

validate () {
  echo "staging production" | grep -F -q -w "$1";
}

deploy () {
  IMAGE="madumbo:languagetool-$1"
  echo "Building $IMAGE"

  TARGET_DIR="languagetool-standalone/target/LanguageTool-3.8-SNAPSHOT/LanguageTool-3.8-SNAPSHOT/"

  echo "Removing $TARGET_DIR"
  rm -rf $TARGET_DIR
  build

  cp Dockerfile $TARGET_DIR
  cp config.properties $TARGET_DIR
  cd $TARGET_DIR

  $(aws --region eu-central-1 ecr get-login)
  docker build -t $IMAGE .
  docker tag $IMAGE 866953695171.dkr.ecr.eu-central-1.amazonaws.com/$IMAGE
  docker push 866953695171.dkr.ecr.eu-central-1.amazonaws.com/$IMAGE
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


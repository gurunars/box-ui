#!/bin/bash

set -eu

GITHUB_ACCOUNT=gurunars
PROJECT_NAME=android-crud-ui

TMP=/tmp/pages.${PROJECT_NAME}

./gradlew test connectedAndroidTest generateReleaseJavadoc bintrayUpload
rm -rf ${TMP}
mv core/build/docs/javadoc ${TMP}
cd ${TMP}
git init
git checkout -b gh-pages
git add .
git commit -am init
git remote add origin git@github.com:${GITHUB_ACCOUNT}/${PROJECT_NAME}.git
git push origin gh-pages -f

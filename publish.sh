#!/bin/bash

set -eu

GITHUB_ACCOUNT=gurunars
PROJECT_NAME=android-crud-ui

TMP=/tmp/pages.${PROJECT_NAME}

./gradlew --rerun-tasks dokka bintrayUpload

git push origin master --tags

rm -rf ${TMP}
mv html-docs ${TMP}

cd ${TMP}
git init
git checkout -b gh-pages
echo "android-crud-ui.gurunars.com" > CNAME
git add .
git commit -am init
git remote add origin git@github.com:${GITHUB_ACCOUNT}/${PROJECT_NAME}.git
git push origin gh-pages -f

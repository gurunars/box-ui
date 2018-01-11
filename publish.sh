#!/bin/bash

set -eu

GITHUB_ACCOUNT=gurunars
PROJECT_NAME=box-ui
DOMAIN_NAME=box-ui.gurunars.com

TMP=/tmp/pages.${PROJECT_NAME}

./gradlew --rerun-tasks dokka bintrayUpload

rm -rf ${TMP}
mv html-docs ${TMP}

cd ${TMP}
git init
git checkout -b gh-pages
echo "${DOMAIN_NAME}" > CNAME
git add .
git commit -am init
git config user.email publisher@gurunars.com
git config user.name Publisher
git remote add origin git@github.com:${GITHUB_ACCOUNT}/${PROJECT_NAME}.git
git push origin gh-pages -f

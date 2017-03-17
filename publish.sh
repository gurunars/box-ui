#!/bin/bash

set -eu

GITHUB_ACCOUNT=gurunars
PROJECT_NAME=android-crud-ui
declare -a PUBLIC_MODULES=("android-utils" "floatmenu" "item-list" "leaflet-view" "crud-item-list")

TMP=/tmp/pages.${PROJECT_NAME}

./gradlew test connectedAndroidTest generateReleaseJavadoc bintrayUpload
rm -rf ${TMP}
mkdir ${TMP}

for module in "${PUBLIC_MODULES[@]}"; do
    mv ${module}/build/docs/javadoc ${TMP}/${module}
done

cd ${TMP}
git init
git checkout -b gh-pages
git add .
git commit -am init
git remote add origin git@github.com:${GITHUB_ACCOUNT}/${PROJECT_NAME}.git
git push origin gh-pages -f

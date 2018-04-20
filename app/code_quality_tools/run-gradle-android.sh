#!/usr/bin/env bash

# Copyright (c) 2015 noboru-i
# https://github.com/noboru-i/android-saddler-sample/blob/master/scripts/saddler.sh

echo "********************"
echo "* install gems     *"
echo "********************"
gem install --no-document checkstyle_filter-git saddler saddler-reporter-github findbugs_translate_checkstyle_format android_lint_translate_checkstyle_format pmd_translate_checkstyle_format

if [ $? -ne 0 ]; then
    echo 'Failed to install gems.'
    exit 1
fi

echo "********************"
echo "* save outputs     *"
echo "********************"

if [ -z "${CI_PULL_REQUEST}" ]; then
    # when not pull request
    REPORTER=Saddler::Reporter::Github::CommitReviewComment
else
    REPORTER=Saddler::Reporter::Github::PullRequestReviewComment
fi

echo "********************"
echo "* checkstyle       *"
echo "********************"
cat app/build/reports/checkstyle/checkstyle.xml \
    | checkstyle_filter-git diff origin/master \
    | saddler report --require saddler/reporter/github --reporter $REPORTER
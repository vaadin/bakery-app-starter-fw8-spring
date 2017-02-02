#!/usr/bin/env bash

# TRAVIS_PULL_REQUEST == "false" for a normal branch commit, the PR number for a PR
# TRAVIS_BRANCH == target of normal commit or target of PR
# TRAVIS_SECURE_ENV_VARS == true if encrypted variables, e.g. SONAR_HOST is available
# TRAVIS_REPO_SLUG == the repository, e.g. vaadin/vaadin

# Exclude the compiled theme from Sonar analysis
SONAR_EXCLUSIONS=src/main/webapp/VAADIN/themes/orderstheme/styles.css

if [ "$TRAVIS_PULL_REQUEST" != "false" ] && [ "$TRAVIS_SECURE_ENV_VARS" == "true" ]
then
	# Pull request for master with secure vars (SONAR_GITHUB_OAUTH, SONAR_HOST) available
	mvn -B -e -V -Dvaadin.testbench.developer.license=$TESTBENCH_LICENSE -Dsonar.verbose=true -Dsonar.analysis.mode=issues -Dsonar.github.repository=$TRAVIS_REPO_SLUG -Dsonar.host.url=$SONAR_HOST -Dsonar.github.oauth=$SONAR_GITHUB_OAUTH -Dsonar.login=$SONAR_LOGIN -Dsonar.github.pullRequest=$TRAVIS_PULL_REQUEST -Dsonar.exclusions=$SONAR_EXCLUSIONS org.jacoco:jacoco-maven-plugin:prepare-agent verify sonar:sonar
elif [ "$TRAVIS_PULL_REQUEST" == "false" ] && [ "$TRAVIS_BRANCH" == "master" ]
then
	# master build
	mvn -B -e -V -Dmaven.javadoc.skip=false -Dvaadin.testbench.developer.license=$TESTBENCH_LICENSE -Dsonar.verbose=true -Dsonar.analysis.mode=publish -Dsonar.host.url=$SONAR_HOST -Dsonar.login=$SONAR_LOGIN -Dsonar.exclusions=$SONAR_EXCLUSIONS verify sonar:sonar
fi

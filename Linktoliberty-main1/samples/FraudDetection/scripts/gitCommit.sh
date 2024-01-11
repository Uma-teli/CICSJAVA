#!/bin/env sh

cd $1

git add .

# Get current timestamp
timestamp=$(date +"%Y-%m-%d %H:%M:%S")

# Get branch name
branch=$(git rev-parse --abbrev-ref HEAD)

# Get commit author
author=$(git config user.name)

# Get list of modified files
modified_files=$(git diff --name-only)

# Generate commit message
commit_message="${timestamp} - Automatic Commit - ${branch}

Modified files:
${modified_files}"

git commit -m "$commit_message"

GIT_SSH_COMMAND="ssh -i /u/wml/jenkinsPipeline/id_rsa"
git push origin main 

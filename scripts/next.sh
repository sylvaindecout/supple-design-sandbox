#!/bin/sh

#
# This script is used during demonstrations.
#
# Usage:
#      Start an interactive rebase.
#      Mark all commits that need to be shown with the action "edit".
#      Call ./scripts/next.sh to move to the next commit (all changes are cleared).
#

git reset --hard
git clean -df
git rebase --continue

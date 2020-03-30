#!/bin/bash
set -eo pipefail
rm -rf lib
cd function
rm -f Gemfile.lock
bundle config set path '../lib'
bundle install
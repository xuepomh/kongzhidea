#!/bin/bash

# @auth: yuan.liu1@renren-inc.com

cur_path=`pwd`
dir_path=`dirname ${BASH_SOURCE[0]}`
# echo "           \$0: " ${BASH_SOURCE}

cd $dir_path
env_path=`pwd`
echo "env path :" ${env_path}

# Prepare the env
export PYTHONPATH=${env_path}/python-lib/:.:${PYTHONPATH}
export LD_LIBRARY_PATH=${env_path}/lib/:$${env_path}/lib/zeromq/
export PATH=${env_path}/python2.7/bin:$PATH

# You can add your configure above

cd $cur_path

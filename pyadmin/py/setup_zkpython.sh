#!/bin/bash

python ez_setup.py

ease_install ./setuptools-0.6c11-py2.7.egg

tar -zxvf termcolor-1.1.0.tar.gz
cd termcolor-1.1.0 && python setup.py install

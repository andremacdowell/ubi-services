#!/bin/bash

sudo yum clean all
sudo yum -y install mysql-server
sudo /etc/init.d/mysqld restart
sudo chkconfig mysqld on
sudo mysql < ../sql/registry.sql

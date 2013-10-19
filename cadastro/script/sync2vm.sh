#!/bin/bash

sudo rsync -vaic --progress --stats --delete /media/sf_sddl/cadastro/ /home/lac/cadastro/
sudo chown -R lac: /home/lac/cadastro
rsync -vaic --progress --stats --delete /home/lac/cadastro/RegistryWeb/ /home/lac/workspace/RegistryWeb/

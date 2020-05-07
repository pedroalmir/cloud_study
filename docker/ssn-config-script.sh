# https://labs.play-with-docker.com/
# wget https://raw.githubusercontent.com/pedroalmir/cloud_study/master/docker/ssn-config-script.sh
# chmod +x ssn-config-script.sh
# \. ssn-config-script.sh
echo "SSN-Config Script"
echo "Creating folder structure..." 
sudo rm -rf ssn-docker > /dev/null 2>&1
mkdir -p ssn-docker/volumes/dababase > /dev/null 2>&1
mkdir -p ssn-docker/volumes/backend > /dev/null 2>&1
echo "Downloading the docker-compose file..."
wget https://raw.githubusercontent.com/pedroalmir/cloud_study/master/docker/docker-compose.yml -O ssn-docker/docker-compose.yml > /dev/null 2>&1
echo "Downloading the backend war file..."
wget --no-check-certificate 'https://docs.google.com/uc?export=download&id=1-8d_AHJL2cOSY001YqQ6Z43lE3eXTCep' -O ssn-docker/volumes/backend/ssnetwork-docker.war > /dev/null 2>&1
echo "Downloading and unzipping the frontend project..."
wget --no-check-certificate 'https://docs.google.com/uc?export=download&id=1yUy4sINTdj9UqYDiCu94WExU1nDB842C' -O ssn-docker/volumes/frontend.zip > /dev/null 2>&1
unzip -qq ssn-docker/volumes/frontend.zip -d ssn-docker/volumes/
echo "Done!"
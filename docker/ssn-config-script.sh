# https://labs.play-with-docker.com/
# wget https://raw.githubusercontent.com/pedroalmir/cloud_study/master/docker/ssn-config-script.sh
# \. ssn-config-script.sh
# Dir stucture:
# |_ ssn-docker
#    |_ dockerfile-backend
#    |_ dockerfile-frontend
#    |_ docker-compose.yml
#    |_ dababase
#    |_ backend
#       |_ ssnetwork-docker.war
#    |_ frontend
#       |_ [frontend dirs and files]

echo -e "\e[92mSSN-Config Script"
echo "Creating folder structure..." 
sudo rm -rf ssn-docker
mkdir -p ssn-docker/dababase
mkdir -p ssn-docker/backend

echo "Downloading the docker files..."
wget -nv https://raw.githubusercontent.com/pedroalmir/cloud_study/master/docker/docker-compose.yml -O ssn-docker/docker-compose.yml
wget -nv https://raw.githubusercontent.com/pedroalmir/cloud_study/master/docker/dockerfile-backend -O ssn-docker/dockerfile-backend
wget -nv https://raw.githubusercontent.com/pedroalmir/cloud_study/master/docker/dockerfile-frontend -O ssn-docker/dockerfile-frontend

echo "Downloading the backend war file..."
wget -nv --load-cookies /tmp/cookies.txt "https://docs.google.com/uc?export=download&confirm=$(wget --quiet --save-cookies /tmp/cookies.txt --keep-session-cookies --no-check-certificate 'https://docs.google.com/uc?export=download&id=1-8d_AHJL2cOSY001YqQ6Z43lE3eXTCep' -O- | sed -rn 's/.*confirm=([0-9A-Za-z_]+).*/\1\n/p')&id=1-8d_AHJL2cOSY001YqQ6Z43lE3eXTCep" -O ssn-docker/backend/ssnetwork-docker.war && rm -rf /tmp/cookies.txt

echo "Downloading and unzipping the frontend project..."
wget -nv --no-check-certificate 'https://docs.google.com/uc?export=download&id=1yUy4sINTdj9UqYDiCu94WExU1nDB842C' -O ssn-docker/frontend.zip
unzip -qq ssn-docker/frontend.zip -d ssn-docker/
rm ssn-docker/frontend.zip

echo "Building docker images..."
cd ssn-docker/
docker build -f dockerfile-backend -t pedroalmir/tomcat:1.0 .
docker build -f dockerfile-frontend -t pedroalmir/python:1.0 .

echo "Executing docker compose..."
docker-compose up -d --build
# docker run --rm -d --name backend -p 80:8080 pedroalmir/mytomcat:0.1
echo -e "\e[97mDone! Now, you can use the services!"



#Execute below commands to run docker

sudo docker build -t home-calculator -f docker/Dockerfile .
<br>
sudo docker run --rm -it --name home -p 8080:8080 home-calculator
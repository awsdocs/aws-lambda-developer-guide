container_name=lambda_python_layer_container
docker_image=lambda_python_layer_image:example

docker run -td --name=$container_name $docker_image
docker cp ./requirements.txt $container_name:/

docker exec -i $container_name /bin/bash < ./docker_install.sh
docker cp $container_name:/layer_content.zip layer_content.zip

docker stop $container_name
docker rm $container_name

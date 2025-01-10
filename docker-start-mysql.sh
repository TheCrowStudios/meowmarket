docker run -d --name meowmarket-db -e MYSQL_ROOT_PASSWORD=meowmeow -e MYSQL_USER=crowie -e MYSQL_PASSWORD=meowmeow -v /home/crowie/src/meowmarket/db:/home/crowie/src/meowmarket/db -p 3306:3306 mysql:latest


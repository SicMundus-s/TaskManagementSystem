БД поднимается с docker-compose-db.yaml, тут сразу две БД, одна для сервисов, другая для rabbit-mq 
rabbitMQ поднимается с docker-compose-rabbitmq.yaml
Команда в консоле для запуска контейнеров docker-compose -f "название файла" up -d

RabbitMQ: Если автоматически не создались queue и exchange, то переходим в http://localhost:15672, логин/пароль - guest, в сооствествующих вкладках создаём очереди.

Поскольку задействована микросервисная архитектура, то сервисы необходимо запускать по очереди:
CORE(Является скорее модулем, запустить 1 раз на всякий случай чтобы Hibernate автоматически создал БД) 
EurekaServer
Security
Task-service
RabbitMQ
Gateway

Возможны проблемы при перезапуске сервисов с контроллерами, т.к gateway теряет связь с ними, достаточно просто презапустить gateway


В сервисах есть ролея система, она полностью настроена и готова к использованию, но на данный момент не задействована, SQL скрипт для ролей: 
INSERT INTO role(name)
VALUES ('ROLE_USER');
INSERT INTO role(name)
VALUES ('ROLE_ADMIN');

Свагер доступен по url - http://localhost:{port}/swagger-ui/index.html

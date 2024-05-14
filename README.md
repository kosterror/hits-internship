# Локальный запуск

1. В корне проекта нужно создать файл `.env` и прописать в нем переменные окружения для следующих параметров:
    ```properties
   EMAIL_USERNAME=
   EMAIL_PASSWORD=
    ```
   `EMAIL_USERNAME` и `EMAIL_PASSWORD` - данные для доступа к почтовому серверу от яндекса. Нужно `EMAIL_USERNAME` -
   почтовый адрес ящика, а `EMAIL_PASSWORD` - сгенерированный токен доступа, подробнее можно почитать
   [здесь](https://yandex.ru/support/mail/mail-clients/others.html).

2. После того как файл `.env` создан и заполнен, все можно развернуть с помощью выполнения команды в корне проекта:
   ```bash
   docker-compose up --build
   ```
   После того как контейнеры запустятся, swagger будет доступен
   по [ссылке](http://localhost:8080/swagger-ui/index.html).

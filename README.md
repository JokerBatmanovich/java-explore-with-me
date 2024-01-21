# java-explore-with-me

### Pull-request: (https://github.com/JokerBatmanovich/java-explore-with-me/pull/6)


### Endpoints

| METHOD       | URI                                                                     | DESCRIPTION                                              | REQUEST CLASS       | RESPONSE CLASS              |
|--------------|-------------------------------------------------------------------------|----------------------------------------------------------|---------------------|-----------------------------|
| ```POST```   | ```/users/{userId}/events/{eventId}/comments?replyTo```                 | Добавить комментарий/ответить на комментарий             | ```NewCommentDto``` | ```CommentDto ```           |
| ```GET```    | ```/users/{userId}/comments?sort&rangeStart&rangeEnd&from&size```       | Получить свои комментарии                                |                     | ```List<CommentDto>```      |
| ```PATCH```  | ```/users/{userid}/comments/{commentId}```                              | Изменить комментарий владельцем                          | ```NewCommentDto``` | ```CommentDto```            |
| ```DELETE``` | ```/users/{userId}/comments/{commentId}```                              | Удалить комментарий владельцем                           |                     |                             |
| ```GET```    | ```/events/{eventId}/comments?sort&start&end&from&size```               | Получить комментарии события                             |                     | ```List<CommentShortDto>``` |
| ```GET```    | ```/comments/{commentId}/replies?sort&start&end&from&size```            | Получить ответы на комментарий                           |                     | ```List<CommentShortDto>``` |
| ```GET```    | ```/comments/{commentId}```                                             | Получить комментарий                                     |                     | ```CommentDto```            |
| ```PATCH```  | ```/users/{userId)/comments/{commentId}/rate&isPositive```              | Оценить комментарий/изменить оценку/отменить оценку      |                     |                             |
| ```GET```    | ```/admin/users/{userId}/comments?sort&rangeStart&rangeEnd&from&size``` | Получить комментарии пользователя от лица администратора |                     | ```List<CommentDto>```      |
| ```DELETE``` | ```/admin/comments/{commentId}```                                       | Удалить комментарий администратором                      |                     |                             |

### Comments
- Положительная оценка комментария повышает его рейтинг на 1, отрицательная - понижает на 1;  
- Комментарии доступны только для опубликованных событий;
- Нельзя дать оценку своему комментарию;
- При удалении пользователя удаляются его комментарии;
- При удалении события удаляются комментарии к нему;
- При удалении комментария, на который был ответ, значение replyTo ответов = null;




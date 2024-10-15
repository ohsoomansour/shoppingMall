------------------------------------------------------------------------------
# 파라미터, 메소드 설명 및 주석
/**
 *@Since: 24.10.14 ~ 
 *@verison: 1.0(AS IS) -> 2.0 (TO BE) 
 *@Author: osooman 
 *@Param: SqlSessionFactory
 *@return: MyBatisBatchItemWriter writer 
 *@Explain: '프로세서'에서 처리된 데이터를 'chunk 단위'로 모아서 한 번에 DB에 쓰기 작업을 수행
 *@see: com.shoppingmall.batch 패키지
 */
------------------------------------------------------------------------------
#[파라미터 구조]

   이름    |   타입   |      설명            |   필수 |
 +--------+---------+-------------------- +-------+
   query  |	String  |   	검색           |   O    |
    page  |  Integer|       결과 페이지 번호 |   X    |


------------------------------------------------------------------------------
#엔드 포인트 구조 && versioning ### 
[AS IS - V.2]
GET  - /api/v1/products/1
       /api/v2/products/2
       /api/v1/users/{userId}
       /api/v2/users/{userId}
       
[TO BE V.1] 
GET - /api/v2/products/1
      /api/v2/products/2

POST - /api/v1/productsInfo/1
       /api/v2/productsInfo/2
 
*명사 복수 사용
*proxy 서버 확인!!
*CORS정책 
------------------------------------------------------------------------------
### 예시 
await(
	await fetch("/api/v2/products/1", {
		headers:{
		  'Authorization' : `Bearer {token}`
		  'Content-Type' : 'application/json'
		},
		methods: 'GET',
		
	})).JOSN()
	

 
#Errors 핸들링
-> known cunsumable format
- 400에러 처리(서버가 클라이언트 에러) 
{
  "error" : {
     "code" : 400,
     "message" :  "Invalid user ID",
     "details": "User ID must be positive integer"
   } 
}
- HTTP status code : 404
{
  "error" : {
     "code" : 404,
     "message" :  "Invalid user ID",
     "details": "Not found"
   } 
}
 - server issue 500 처리 
{
  "error" : {
     "code" : 500,
     "message" :  "Server error",
     "details": "Internal Server Error "
   } 
}

### Authentication & Authority ### 
 - Spring Security (3.3.2) 
 - OAuth2.0 또는 JWT 사용
 